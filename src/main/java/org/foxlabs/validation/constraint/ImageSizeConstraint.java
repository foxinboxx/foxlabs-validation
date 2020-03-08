/*
 * Copyright (C) 2012 FoxLabs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.foxlabs.validation.constraint;

import java.awt.Image;
import java.awt.image.RenderedImage;
import java.awt.image.BufferedImage;

import java.util.Iterator;
import java.util.Map;

import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.foxlabs.validation.AbstractValidation;
import org.foxlabs.validation.ValidationContext;

import org.foxlabs.common.Predicates;

/**
 * This class provides base implementation of the <code>Constraint</code> that
 * checks whether the size of an image is within maximum bounds. Also image
 * size can be adjusted according to {@link ImageAdjust} mode.
 *
 * @author Fox Mulder
 * @param <V> The type of image format
 * @see ImageSize
 * @see ImageAdjust
 */
public abstract class ImageSizeConstraint<V> extends AbstractValidation<V> implements Constraint<V> {

    /**
     * Maximum image width.
     */
    private final int maxWidth;

    /**
     * Maximum image height.
     */
    private final int maxHeight;

    /**
     * Image size adjust mode.
     */
    private final ImageAdjust adjust;

    /**
     * Constructs a new <code>ImageSizeConstraint</code> with the specified
     * maximum bounds and adjust mode.
     *
     * @param maxWidth Maximum image width.
     * @param maxHeight Maximum image height.
     * @param adjust Image size adjust mode.
     * @throws IllegalArgumentException if the specified maximum width or height
     *         is negative or zero or the specified adjust mode is
     *         <code>null</code>.
     */
    protected ImageSizeConstraint(int maxWidth, int maxHeight, ImageAdjust adjust) {
        this.maxWidth = Predicates.require(maxWidth, Predicates.INT_POSITIVE, "maxWidth");
        this.maxHeight = Predicates.require(maxHeight, Predicates.INT_POSITIVE, "maxHeight");
        this.adjust = Predicates.requireNonNull(adjust, "adjust");
    }

    /**
     * Constructs a new <code>ImageSizeConstraint</code> from the specified
     * annotation.
     *
     * @param annotation Constraint annotation.
     * @throws IllegalArgumentException if the specified annotation defines
     *         negative or zero maximum width or height.
     */
    protected ImageSizeConstraint(ImageSize annotation) {
        this(annotation.maxWidth(), annotation.maxHeight(), annotation.adjust());
    }

    /**
     * Returns maximum image width.
     *
     * @return Maximum image width.
     */
    public final int getMaxWidth() {
        return maxWidth;
    }

    /**
     * Returns maximum image height.
     *
     * @return Maximum image height.
     */
    public final int getMaxHeight() {
        return maxHeight;
    }

    /**
     * Returns image size adjust mode.
     *
     * @return Image size adjust mode.
     */
    public final ImageAdjust getAdjustMode() {
        return adjust;
    }

    /**
     * Returns localized error message template.
     *
     * @param context Validation context.
     * @return Localized error message template.
     */
    @Override
    public String getMessageTemplate(ValidationContext<?> context) {
        return context.resolveMessage(ImageSizeConstraint.class.getName());
    }

    /**
     * Appends <code>maxWidth</code> and <code>maxHeight</code> arguments that
     * contain maximum allowed image width and height respectively.
     *
     * @param context Validation context.
     * @param arguments Arguments to be substituted into the error message
     *        template.
     * @return <code>true</code>.
     */
    @Override
    public boolean appendMessageArguments(ValidationContext<?> context, Map<String, Object> arguments) {
        super.appendMessageArguments(context, arguments);
        arguments.put("maxWidth", Integer.toString(maxWidth));
        arguments.put("maxHeight", Integer.toString(maxHeight));
        return true;
    }

    /**
     * Checks whether the size of the specified image is within maximum bounds.
     *
     * @param value Image to be checked.
     * @param context Validation context.
     * @return Image as is if image size is within maximum bounds or adjusted
     *         image if adjust mode differs from {@link ImageAdjust#NONE}.
     * @throws ConstraintViolationException if image size exceeds maximum bounds
     *         and adjust mode is {@link ImageAdjust#NONE}.
     */
    @Override
    public <T> V validate(V value, ValidationContext<T> context) {
        if (value == null)
            return null;
        try {
            Context c = decodeImage(value);
            if (c.image == null)
                return value;
            int w = c.image.getWidth(null);
            int h = c.image.getHeight(null);
            if (w > maxWidth || h > maxHeight) {
                if (adjust == ImageAdjust.NONE)
                    throw new ConstraintViolationException(this, context, value);
                if (adjust == ImageAdjust.CLIP) {
                    c.image = toBufferedImage(c.image).getSubimage(0, 0,
                            Math.min(w, maxWidth), Math.min(h, maxHeight));
                } else if (adjust == ImageAdjust.ZOOM) {
                    double factor = Math.min((double) maxWidth / (double) w, (double) maxHeight / (double) h);
                    c.image = c.image.getScaledInstance((int) (w * factor), (int) (h * factor),
                            Image.SCALE_SMOOTH | Image.SCALE_AREA_AVERAGING);
                }
                return encodeImage(c);
            }
        } catch (IOException e) {}
        return value;
    }

    /**
     * Decodes the specified raw image representation into
     * <code>java.awt.Image</code> and returns context that contains image and
     * its attributes.
     *
     * @param value Raw image representation.
     * @return Context for codecs.
     * @throws IOException if decoding of an image fails.
     */
    protected abstract Context decodeImage(V value) throws IOException;

    /**
     * Encodes <code>java.awt.Image</code> from the specified context and
     * returns raw image representation.
     *
     * @param context Image context.
     * @return Image.
     * @throws IOException if encoding of an image fails.
     */
    protected abstract V encodeImage(Context context) throws IOException;

    // Context

    /**
     * Context to be used by image codecs.
     *
     * @author Fox Mulder
     */
    protected static class Context {

        /**
         * <code>java.awt.Image</code> decoded image.
         */
        protected Image image;

    }

    // AwtCodec

    /**
     * This class provides <code>ImageSizeConstraint</code> implementation for
     * <code>java.awt.Image</code> type.
     *
     * @author Fox Mulder
     * @see ConstraintFactory#awtImageSize(int, int, ImageAdjust)
     */
    public static final class AwtCodec extends ImageSizeConstraint<Image> {

        /**
         * Constructs a new <code>ImageSizeConstraint.AwtCodec</code> with the
         * specified maximum bounds and adjust mode.
         *
         * @param maxWidth Maximum image width.
         * @param maxHeight Maximum image height.
         * @param adjust Image size adjust mode.
         * @throws IllegalArgumentException if the specified maximum width or
         *         height is negative or zero.
         * @throws NullPointerException if the specified adjust mode is
         *         <code>null</code>.
         */
        AwtCodec(int maxWidth, int maxHeight, ImageAdjust adjust) {
            super(maxWidth, maxHeight, adjust);
        }

        /**
         * Constructs a new <code>ImageSizeConstraint.AwtCodec</code> from the
         * specified annotation.
         *
         * @param annotation Constraint annotation.
         * @throws IllegalArgumentException if the specified annotation defines
         *         negative or zero maximum width or height.
         */
        AwtCodec(ImageSize annotation) {
            super(annotation);
        }

        /**
         * Returns <code>java.awt.Image</code> type.
         *
         * @return <code>java.awt.Image</code> type.
         */
        @Override
        public Class<?> getType() {
            return Image.class;
        }

        /**
         * Returns context that contains the specified image.
         *
         * @param value <code>java.awt.Image</code> object.
         */
        @Override
        protected Context decodeImage(Image value) {
            Context c = new Context();
            c.image = value;
            return c;
        }

        /**
         * Returns <code>java.awt.Image</code> from the specified context.
         *
         * @param context Image context.
         */
        @Override
        protected Image encodeImage(Context context) {
            return context.image;
        }

    }

    // RawCodec

    /**
     * Context used by <code>ImageSizeConstraint.RawCodec</code>.
     *
     * @author Fox Mulder
     */
    protected static class IOContext extends Context {

        /**
         * Image format name.
         */
        protected String format;

    }

    /**
     * This class provides <code>ImageSizeConstraint</code> implementation for
     * <code>byte[]</code> type.
     *
     * @author Fox Mulder
     * @see ConstraintFactory#rawImageSize(int, int, ImageAdjust)
     */
    public static final class RawCodec extends ImageSizeConstraint<byte[]> {

        /**
         * Constructs a new <code>ImageSizeConstraint.RawCodec</code> with the
         * specified maximum bounds and adjust mode.
         *
         * @param maxWidth Maximum image width.
         * @param maxHeight Maximum image height.
         * @param adjust Image size adjust mode.
         * @throws IllegalArgumentException if the specified maximum width or
         *         height is negative or zero.
         * @throws NullPointerException if the specified adjust mode is
         *         <code>null</code>.
         */
        RawCodec(int maxWidth, int maxHeight, ImageAdjust adjust) {
            super(maxWidth, maxHeight, adjust);
        }

        /**
         * Constructs a new <code>ImageSizeConstraint.RawCodec</code> from the
         * specified annotation.
         *
         * @param annotation Constraint annotation.
         * @throws IllegalArgumentException if the specified annotation defines
         *         negative or zero maximum width or height.
         */
        RawCodec(ImageSize annotation) {
            super(annotation);
        }

        /**
         * Returns <code>byte[]</code> type.
         *
         * @return <code>byte[]</code> type.
         */
        @Override
        public Class<?> getType() {
            return byte[].class;
        }

        /**
         * Decodes the specified <code>byte[]</code> array representing an
         * image into <code>java.awt.Image</code> and returns context that
         * contains image and its format name.
         *
         * @param value <code>byte[]</code> image representation.
         * @throws IOException if decoding of an image fails.
         */
        @Override
        protected Context decodeImage(byte[] value) throws IOException {
            IOContext c = new IOContext();
            ImageInputStream input = ImageIO.createImageInputStream(new ByteArrayInputStream(value));
            Iterator<ImageReader> itr = ImageIO.getImageReaders(input);
            if (itr.hasNext()) {
                ImageReader reader = itr.next();
                reader.setInput(input);
                c.format = reader.getFormatName();
                c.image = reader.read(0, reader.getDefaultReadParam());
            }
            return c;
        }

        /**
         * Encodes <code>java.awt.Image</code> from the specified context and
         * returns <code>byte[]</code> array representing that image.
         *
         * @param context Image context.
         * @throws IOException if encoding of an image fails.
         */
        @Override
        protected byte[] encodeImage(Context context) throws IOException {
            IOContext c = (IOContext) context;
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            RenderedImage image = c.image instanceof RenderedImage
                ? (RenderedImage) c.image
                : toBufferedImage(c.image);
            ImageIO.write(image, c.format, output);
            return output.toByteArray();
        }

    };

    // Helper methods

    /**
     * Converts <code>java.awt.Image</code> into
     * <code>java.awt.image.BufferedImage</code>.
     *
     * @param source <code>java.awt.Image</code> object.
     * @return <code>java.awt.image.BufferedImage</code> object.
     */
    public static BufferedImage toBufferedImage(Image source) {
        if (source instanceof BufferedImage)
            return (BufferedImage) source;
        int w = source.getWidth(null);
        int h = source.getHeight(null);
        BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        image.getGraphics().drawImage(source, 0, 0, w, h, 0, 0, w, h, null);
        return image;
    }

}
