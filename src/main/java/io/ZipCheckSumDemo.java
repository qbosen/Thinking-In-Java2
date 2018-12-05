package io;

import custom.Utils;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.nio.charset.Charset;
import java.util.zip.*;

/**
 * @author qiubaisen
 * @date 2018-12-05
 */

@SuppressWarnings("Duplicates")
public class ZipCheckSumDemo {

    private static final Charset UTF_8 = Charset.forName("UTF-8");

    public long zip(String zipName, String... filenames) throws IOException {
        CheckedOutputStream checkedOutputStream = new CheckedOutputStream(new FileOutputStream(zipName), new Adler32());
        ZipOutputStream zipOutputStream = new ZipOutputStream(new BufferedOutputStream(checkedOutputStream), UTF_8);

        System.out.println("开始压缩文件");
        for (String filename : filenames) {
            System.out.println("压缩：" + filename);

            File file = new File(filename);
            BufferedInputStream fileReader = new BufferedInputStream(new FileInputStream(file));
            String fileShortName = file.getName();
            int endIndex = fileShortName.lastIndexOf('.');
            String fileShortNameWithoutExt = endIndex == -1 ? fileShortName : fileShortName.substring(0, endIndex);
            String zipFileName = fileShortNameWithoutExt + ".txt";

            zipOutputStream.putNextEntry(new ZipEntry(zipFileName));
            int i;
            while ((i = fileReader.read()) != -1) {
                zipOutputStream.write(i);
            }
            fileReader.close();
            zipOutputStream.flush();
        }
        zipOutputStream.close();
        return printCheckSum(checkedOutputStream);
    }

    public long unzip(String zipFile) throws IOException {
        CheckedInputStream checkedInputStream = new CheckedInputStream(new FileInputStream(zipFile), new Adler32());
        ZipInputStream zipInputStream = new ZipInputStream(new BufferedInputStream(checkedInputStream));

        ZipEntry zipEntry;
        while ((zipEntry = zipInputStream.getNextEntry()) != null) {
            System.out.println("解压：" + zipEntry.getName());
            BufferedOutputStream fileWriter = new BufferedOutputStream(new FileOutputStream(zipEntry.getName()));
            int i;
            while ((i = zipInputStream.read()) != -1) {
                fileWriter.write(i);
            }
            fileWriter.close();
        }
        zipInputStream.close();
        return printCheckSum(checkedInputStream);
    }


    private long printCheckSum(CheckedOutputStream checkedOutputStream) {
        return printCheckSum(checkedOutputStream.getChecksum());
    }

    private long printCheckSum(CheckedInputStream checkedInputStream) {
        return printCheckSum(checkedInputStream.getChecksum());
    }

    private Long printCheckSum(Checksum checksum) {
        long value = checksum.getValue();
        System.out.println(value);
        return value;
    }


    @Test
    public void testCreateOneFileZip() throws IOException {
        zip("zipDemo.zip", Utils.relativeFilePath(ZipCompress.class));
    }

    @Test
    public void testCreateTwoFileZip() throws IOException {
        zip("zipDemo.zip", Utils.relativeFilePath(ZipCompress.class), Utils.relativeFilePath(ZipCheckSumDemo.class));
    }

    @Test
    public void testUnzip() throws IOException {
        unzip("zipDemo.zip");
    }

    @Test
    public void testCheckSum() throws IOException {
        String zipName = "zipDemo.zip";
        long zip = zip(zipName, Utils.relativeFilePath(ZipCompress.class), Utils.relativeFilePath(ZipCheckSumDemo.class));
        long unzip = unzip(zipName);
        Assert.assertEquals(zip, unzip);
    }
}
