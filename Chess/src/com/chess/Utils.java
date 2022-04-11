package com.chess;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.batik.transcoder.SVGAbstractTranscoder;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.ImageTranscoder;
import org.apache.commons.io.FileUtils;

public class Utils {

	private Utils() {
		throw new IllegalStateException("Utility class");
	}

	public static class BufferedImageTranscoder extends ImageTranscoder {
		private BufferedImage img = null;

		@Override
		public BufferedImage createImage(int w, int h) {
			return new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		}

		@Override
		public void writeImage(BufferedImage img, TranscoderOutput output) {
			this.img = img;
		}

		public BufferedImage getBufferedImage() {
			return img;
		}
	}

	public static BufferedImage loadImage(File svgFile, float width, float height) {

		InputStream targetStream = null;
		try {
			targetStream = FileUtils.openInputStream(svgFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		BufferedImageTranscoder imageTranscoder = new BufferedImageTranscoder();

		imageTranscoder.addTranscodingHint(SVGAbstractTranscoder.KEY_WIDTH, width);
		imageTranscoder.addTranscodingHint(SVGAbstractTranscoder.KEY_HEIGHT, height);

		TranscoderInput input = new TranscoderInput(targetStream);
		try {
			imageTranscoder.transcode(input, null);
		} catch (TranscoderException e) {
			e.printStackTrace();
		}

		return imageTranscoder.getBufferedImage();
	}

	public static Color mixColors(Color... colors) {
		float ratio = 1f / (colors.length);
		int r = 0, g = 0, b = 0, a = 0;
		for (Color color : colors) {
			r += color.getRed() * ratio;
			g += color.getGreen() * ratio;
			b += color.getBlue() * ratio;
			a += color.getAlpha() * ratio;
		}
		return new Color(r, g, b, a);
	}
}
