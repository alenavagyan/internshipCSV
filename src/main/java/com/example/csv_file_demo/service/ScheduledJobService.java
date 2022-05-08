package com.example.csv_file_demo.service;

import com.example.csv_file_demo.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class ScheduledJobService {

    @Autowired
    BookRepository bookRepository;

    String link = "http://images.amazon.com/images/P/0195153448.01.LZZZZZZZ.jpg";
    File out = new File("D:\\Prgramming\\Java\\Image_Downloading_Demo\\foto\\New Foto.png");

    @Scheduled(fixedRate = 5000)
    public void downloadingImage() {
        try {

            URL url = new URL(link);
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            double fileSize = (double) http.getContentLengthLong();
            BufferedInputStream in = new BufferedInputStream(http.getInputStream());
            FileOutputStream fos = new FileOutputStream(this.out);
            BufferedOutputStream bout = new BufferedOutputStream(fos, 1024);
            byte[] buffer = new byte[1024];
            double downloaded = 0.00;
            int read = 0;
            double percentDownloaded = 0.00;
            while ((read = in.read(buffer, 0, 1024)) >= 0) {
                bout.write(buffer, 0, read);
                downloaded += read;
                percentDownloaded = (downloaded * 100) / fileSize;
                String percent = String.format("%.4f", percentDownloaded);
                System.out.println("Downloaded " + percent + "% of a file.");
            }
            bout.close();
            in.close();
            System.out.println("Download complete");

        } catch (
                IOException ex) {
            ex.printStackTrace();
        }
    }
}
