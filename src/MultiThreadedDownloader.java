
/*
* Создайте приложение, которое скачивает файлы из интернета в несколько потоков.
У пользователя должна быть возможность указать, сколько потоков использовать для загрузки.
* */


import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;
import javax.net.ssl.HttpsURLConnection;

public class MultiThreadedDownloader {
    private static final int BUFFER_SIZE = 4096;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

    //    System.out.println("Введите URL файла для загрузки:");
    //    String fileURL = scanner.nextLine();
        String fileURL = "https://gbcdn.mrgcdn.ru/uploads/asset/5580434/attachment/95be3ec2a603237daef4460180ab46ed.pdf";
        System.out.println("Введите количество потоков для загрузки:");
        int threadCount = scanner.nextInt();
     //   int threadCount = 2;

        String saveFilePath = "D:\\temp\\"+ parseFileName(fileURL);
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        List<Future<?>> futures = new ArrayList<>();

        for (int i = 0; i < threadCount; i++) {
            int partNumber = i;
            futures.add(executor.submit(() -> downloadFilePart(fileURL, saveFilePath, partNumber, threadCount)));
        }

        for (Future<?> future : futures) {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        executor.shutdown();
        System.out.println("Загрузка завершена.");
    }

    private static void downloadFilePart(String fileURL, String saveFilePath, int partNumber, int threadCount) {
        HttpsURLConnection httpsConn = null;
        try {
            URL url = new URL(fileURL);
            httpsConn = (HttpsURLConnection) url.openConnection();
            int contentLength = httpsConn.getContentLength();
            int partSize = contentLength / threadCount;

            int startByte = partNumber * partSize;
            int endByte = (partNumber == threadCount - 1) ? contentLength : (startByte + partSize - 1);

            httpsConn.disconnect();
            httpsConn = (HttpsURLConnection) url.openConnection();
            httpsConn.setRequestProperty("Range", "bytes=" + startByte + "-" + endByte);

            try (InputStream inputStream = httpsConn.getInputStream();
                 RandomAccessFile outputFile = new RandomAccessFile(saveFilePath, "rw")) {
                outputFile.seek(startByte);
                byte[] buffer = new byte[BUFFER_SIZE];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputFile.write(buffer, 0, bytesRead);
                }
            }
            System.out.println("Часть " + partNumber + " загружена.");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (httpsConn != null) {
                httpsConn.disconnect();
            }
        }
    }

    private static String parseFileName(String fileURL) {
        try {
            URL url = new URL(fileURL);
            String filePath = url.getPath();
            return filePath.substring(filePath.lastIndexOf('/') + 1);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return "default_file_name";
        }
    }
}
