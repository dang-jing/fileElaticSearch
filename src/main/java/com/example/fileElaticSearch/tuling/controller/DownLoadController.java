package com.example.fileElaticSearch.tuling.controller;


import com.alibaba.fastjson.JSONObject;
import com.example.fileElaticSearch.tuling.utils.CompactAlgorithm;
import com.example.fileElaticSearch.tuling.utils.ESInterface;
import com.example.fileElaticSearch.tuling.utils.Zip;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * @Author：小党
 * @Date:Created in 17:472021/4/25
 * @Explain：
 */
@RestController
@ResponseBody
public class DownLoadController {
    private final static String utf8 = "utf-8";

    @Autowired
    private ESInterface esUtils;
    @Autowired
    private CompactAlgorithm compactAlgorithm;
    @Autowired
    private Zip zip;


    @RequestMapping(value = "/download", method = {RequestMethod.GET})
    public void downLoadFikle(HttpServletResponse response, String index, String model, String type) throws IOException {
        if ("截图".equals(index)) {
            index = "jt";
        } else if ("拍照".equals(index)) {
            index = "pz";
        } else {
            index = "";
        }
        String fileName = esUtils.search(index, model, type);
        // 将最后一个\\前边的内容全部替换成空
        String downloadName = URLEncoder.encode(fileName.replaceAll(".*\\\\", "") + ".zip", utf8);

        //将文件进行打包下载
        try {
            OutputStream out = response.getOutputStream();
            byte[] data = zip.createZip(fileName);//服务器存储地址
            response.reset();
            response.setHeader("Content-Disposition", "attachment;fileName=" + downloadName);
            response.addHeader("Content-Length", "" + data.length);
            response.setContentType("application/octet-stream;charset=" + utf8);
            IOUtils.write(data, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PostMapping(value = "upload")
    public void upload(MultipartFile mFile, JSONObject json) {
        try {
            //将浏览器文件，转为本地临时文件   transferToFile(mFile)
            File file = new File("C:\\Users\\dangc\\Pictures\\标注2原图");
            //将本地临时临时文件解压到指定目录下,返回文件夹路径
            String path = unZip(file, "C:\\Users\\dangc\\Pictures\\Saved Pictures");
            //删除本地临时文件
            delteTempFile(file);
            //获取到加压后文件夹内所有文件路径
            List<String> paths = getDataDeployList(path);
            //将解压后文件夹内容与指定文件夹整合
            copy(paths, "C:\\Users\\dangc\\Pictures\\Saved Pictures");
            //删除解压后的文件夹
            deleteDir(path);

        } catch (Exception e) {
            System.out.println("读取文件失败");
        }
    }


    /**
     * zip解压
     *
     * @param srcFile     zip源文件
     * @param destDirPath 解压后的目标文件夹
     *                    返回文件夹名字
     * @throws RuntimeException 解压失败会抛出运行时异常
     */

    public static String unZip(File srcFile, String destDirPath) throws RuntimeException {

        long start = System.currentTimeMillis();

        // 判断源文件是否存在

        if (!srcFile.exists()) {

            throw new RuntimeException(srcFile.getPath() + "所指文件不存在");

        }

        // 开始解压

        ZipFile zipFile = null;

        try {

            zipFile = new ZipFile(srcFile, Charset.forName("GBK"));

            Enumeration<?> entries = zipFile.entries();
            String dirPath = null;

            while (entries.hasMoreElements()) {

                ZipEntry entry = (ZipEntry) entries.nextElement();

                System.out.println("解压" + entry.getName());

                // 如果是文件夹，就创建个文件夹

                if (entry.isDirectory()) {

                    dirPath = destDirPath + "/" + entry.getName();

                    File dir = new File(dirPath);

                    dir.mkdirs();

                } else {

                    // 如果是文件，就先创建一个文件，然后用io流把内容copy过去

                    File targetFile = new File(destDirPath + "/" + entry.getName());

                    // 保证这个文件的父文件夹必须要存在

                    if (!targetFile.getParentFile().exists()) {

                        targetFile.getParentFile().mkdirs();

                    }

                    targetFile.createNewFile();

                    // 将压缩文件内容写入到这个文件中

                    InputStream is = zipFile.getInputStream(entry);

                    FileOutputStream fos = new FileOutputStream(targetFile);

                    int len;

                    byte[] buf = new byte[1024];

                    while ((len = is.read(buf)) != -1) {

                        fos.write(buf, 0, len);

                    }

                    // 关流顺序，先打开的后关闭

                    fos.close();

                    is.close();

                }

            }

            long end = System.currentTimeMillis();

            System.out.println("解压完成，耗时：" + (end - start) + " ms");
            return dirPath;

        } catch (Exception e) {

            throw new RuntimeException("unzip error from ZipUtils", e);

        } finally {

            if (zipFile != null) {

                try {

                    zipFile.close();

                } catch (IOException e) {

                    e.printStackTrace();

                }

            }

        }

    }


    /**
     * MultipartFile 转 File
     *
     * @param file
     * @throws Exception
     */
    public static File multipartFileToFile(MultipartFile file) {

        File toFile = null;
        if (file.equals("") || file.getSize() <= 0) {
            file = null;
        } else {
            try {
                InputStream ins = null;
                ins = file.getInputStream();
                toFile = new File(file.getOriginalFilename());
                inputStreamToFile(ins, toFile);
                ins.close();
            } catch (Exception e) {
                System.out.println("io错误");
            }

        }
        return toFile;
    }

    //获取流文件
    public static void inputStreamToFile(InputStream ins, File file) {
        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除本地临时文件
     *
     * @param file
     */
    public static void delteTempFile(File file) {
        if (file != null) {
            File del = new File(file.toURI());
            del.delete();
        }
    }


    public static void main(String[] args) throws Exception {

        //将浏览器文件，转为本地临时文件   transferToFile(mFile)
        File file = new File("C:\\Users\\dangc\\Pictures\\标注2原图.zip");
        //将本地临时临时文件解压到指定目录下,返回文件夹路径
        String path = unZip(file, "C:\\Users\\dangc\\Pictures\\Saved Pictures");
        System.out.println(path);
        //删除本地临时文件
        delteTempFile(file);
        //获取到加压后文件夹内所有文件路径
        List<String> paths = getDataDeployList(path);
        //将解压后文件夹内容与指定文件夹整合
        copy(paths, "C:\\Users\\dangc\\Pictures\\Saved Pictures");
        //删除解压后的文件夹
        deleteDir(path);

        /*String newPath = "C:\\Users\\dangc\\Pictures\\Saved Pictures";
        //String oldFile = unZip(new File("C:\\Users\\dangc\\Pictures\\标注2原图.zip"), newFile);
        System.out.println("");
        System.out.println(newPath);
        List<String> paths = getDataDeployList("C:\\Users\\dangc\\Pictures\\Saved Pictures\\标注2原图");
        copy(paths,newPath);
        //delteTempFile(new File("C:\\Users\\dangc\\Pictures\\Saved Pictures\\标注2原图"));
        deleteDir("C:\\\\Users\\\\dangc\\\\Pictures\\\\Saved Pictures\\\\标注2原图");*/
    }

    /**
     * 迭代删除文件夹
     *
     * @param dirPath 文件夹路径
     */
    public static void deleteDir(String dirPath) {
        File file = new File(dirPath);
        if (file.isFile()) {
            file.delete();
        } else {
            File[] files = file.listFiles();
            if (files == null) {
                file.delete();
            } else {
                for (int i = 0; i < files.length; i++) {
                    deleteDir(files[i].getAbsolutePath());
                }
                file.delete();
            }
        }
    }


    /**
     * @Author: dang on 2021/4/30 11:35
     * @Param paths 文件夹下所有文件路径
     * @Param newpath 指定文件夹
     * @return:void
     * @description: 将一个文件夹内容合并到另一个文件夹下
     */
    public static void copy(List<String> paths, String newpath) {

        try {
            for (String path : paths) {
                FileInputStream in = new FileInputStream(path);
                byte[] buffer = new byte[1024];
                FileOutputStream os = new FileOutputStream(newpath + "\\" + path.replaceAll(".*\\\\", ""));
                while (in.read(buffer) != -1) {
                    os.write(buffer, 0, buffer.length);
                }
                os.close();
                in.close();
            }
        } catch (Exception e) {
            System.out.println("io流错误");
        }
    }


    /**
     * @Author: dang on 2021/4/30 11:36
     * @Param filepath  文件夹
     * @return:java.util.List<java.lang.String> 文件夹下所有文件路径
     * @description: 获取文件夹下所有文件路径
     */
    public static List<String> getDataDeployList(String filepath) {

        File file = new File(filepath);
        File[] tempList;
        List<String> list = new ArrayList<String>();
        if (file.isDirectory()) {
            tempList = file.listFiles();
            list = new ArrayList<String>();
            for (int i = 0; i < tempList.length; i++) {
                if (tempList[i].isFile()) {
                    //  System.out.println(tempList[i].getAbsolutePath());
                    list.add(tempList[i].getAbsolutePath());
                }
                if (tempList[i].isDirectory()) {
                    list.addAll(getDataDeployList(tempList[i].getAbsolutePath()));
                }
            }
        } else if (file.isFile()) {
            String s = file.getAbsolutePath();
            list.add(s);
        }
        return list;
    }

    /**
     * @Author: dang on 2021/4/28 16:21
     * @Param multipartFile
     * @return:java.io.File
     * @description: multipartFile转为file
     */
    private static File transferToFile(MultipartFile multipartFile) {

//        选择用缓冲区来实现这个转换即使用java 创建的临时文件 使用 MultipartFile.transferto()方法 。
        File file = null;
        try {
            String originalFilename = multipartFile.getOriginalFilename();
            String[] filename = originalFilename.split(".");
            file = File.createTempFile(filename[0], filename[1]);
            multipartFile.transferTo(file);
            file.deleteOnExit();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * 浏览器分片下载
     *
     * @Author: dang on 2021/4/27 10:22
     * @Param null
     * @return:
     * @description:
     */
    public void aa() {
        /*response.setCharacterEncoding(utf8);
        InputStream is = null;
        OutputStream os = null;
        try {
            //分片下载，客户端需要合并
            long fSize = file.length();
            //下载类型
            response.setContentType("application/x-download");
            //设置对话框中默认文件名
            String fileName = URLEncoder.encode(file.getName(), utf8);
            //弹出对话框，默认名：attachment
            response.addHeader("content-Disposition", "attachment;filename=" + fileName);
            //告诉前端支持分片下载
            response.setHeader("Accept-Range", "bytes");
            //告诉前端文件大小/文件名
            response.setHeader("fSize", String.valueOf(fSize));
            response.setHeader("fName", String.valueOf(fileName));
            //记录分片的默认值
            long pos = 0, last = fSize - 1, sum = 0;
            //判断是否分片
            if (null != request.getHeader("Range")) {
                response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
                //获取分片信息，
                String numRange = request.getHeader("Range").replaceAll("bytes=", "");
                String[] strRange = numRange.split("-");
                if (strRange.length == 2) {
                    pos = Long.parseLong(strRange[0].trim());
                    last = Long.parseLong(strRange[1].trim());
                    if (last > fSize - 1) {
                        last = fSize - 1;
                    }
                } else {
                    pos = Long.parseLong(numRange.replaceAll("-", "").trim());
                }
            }

            long rangeLenght = last - pos + 1;
            String contentRange = new StringBuffer("bytes ").append(pos).append("-").append(last).append("/").append(fSize).toString();
            response.setHeader("Content-Range", contentRange);
            response.setHeader("Content-lenght", String.valueOf(rangeLenght));

            os = new BufferedOutputStream(response.getOutputStream());
            is = new BufferedInputStream(new FileInputStream(file));
            is.skip(pos);
            byte[] buffer = new byte[1024];
            int lenght = 0;
            while (sum < rangeLenght) {
                lenght = is.read(buffer, 0, (rangeLenght - sum) <= buffer.length ? (int) (rangeLenght - sum) : buffer.length);
                sum=sum+lenght;
                os.write(buffer,0,lenght);
            }

            System.out.println("下载完成");
        } finally {
            //保证输入输出流的关闭
            if (is != null) {
                is.close();
            }
            if (os != null) {
                os.close();
            }
        }*/
    }

    /**
     * 压缩失败
     *
     * @Author: dang on 2021/4/27 10:21
     * @Param null
     * @return:
     * @description:
     */
    public static void zipFiles(File file, String zipFileName, HttpServletResponse response) throws IOException {
        byte[] buf = new byte[1024];
        // 获取输出流
        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileInputStream in = null;
        ZipOutputStream out = null;
        try {
            response.reset(); // 重点突出
            // 不同类型的文件对应不同的MIME类型
            response.setContentType("application/x-msdownload");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-disposition", "attachment;filename=" + zipFileName + ".zip");

            // ZipOutputStream类：完成文件或文件夹的压缩
            out = new ZipOutputStream(bos);

            in = new FileInputStream(file);
            // 给列表中的文件单独命名
            out.putNextEntry(new ZipEntry(file.getName()));
            int len = -1;
            while ((len = in.read(buf)) != -1) {
                out.write(buf, 0, len);
            }


            out.close();
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) in.close();
            if (out != null) out.close();
        }
    }


}
