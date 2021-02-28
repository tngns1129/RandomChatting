package com.familyset.randomchatting.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class DiskIOUtil {
    private DiskIOUtil() {}

    public static String size2String(Long fileSize) {
        Integer unit = 1024;
        if (fileSize < unit) {
            return String.format("%d bytes", fileSize);
        }
        int exp = (int) (Math.log(fileSize) / Math.log(unit));

        return String.format("%.0f %sbytes", fileSize / Math.pow(unit, exp), "KMGTPE".charAt(exp-1));
    }

    public static String getResizedTempFilePath(String path, @Nullable Integer reqWidth, @Nullable Integer reqHeight) throws IOException {
        // 임시 파일 생성
        File tempFile = File.createTempFile("image", "jpg");

        FileOutputStream out = new FileOutputStream(tempFile);

        // 용량 감소한 비트맵 생성
        Bitmap bitmap = decodeBitmapFromImagePath(path, reqWidth, reqHeight);

        // 비트맵을 JPEG 파일로 변환
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

        bitmap.recycle();
        out.close();
        tempFile.deleteOnExit();

        return tempFile.getPath();
    }

    public static Bitmap decodeBitmapFromImagePath(String path, @Nullable Integer reqWidth, @Nullable Integer reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        // 메모리 할당 방지, 이미지 디코딩
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // 축소할 사이즈 결정
        options.inSampleSize = calculateInSampleSize(options,
                (reqWidth == null) ? reqWidth : 1080, (reqHeight == null) ? reqHeight : 1920);

        // 메모리 할당 설정, 축소할 사이즈로 이미지 디코딩
        options.inJustDecodeBounds = true;
        return BitmapFactory.decodeFile(path, options);
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // 원본 이미지 높이, 너비
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // 요구 사이즈 보다 클 경우 2배씩 감소
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}
