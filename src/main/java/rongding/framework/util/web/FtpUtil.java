package rongding.framework.util.web;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import sun.net.TelnetInputStream;
import sun.net.TelnetOutputStream;
import sun.net.ftp.FtpClient;

public class FtpUtil {    
	private static final Logger logger = Logger.getLogger(FtpUtil.class);
	
    private String ip = "";      
    private String username = "";      
    private String password = "";      
    private int port = -1;      
    private String path = "";      
    FtpClient ftpClient = null;      
    OutputStream os = null;      
    FileInputStream is = null;      
     
    public FtpUtil(String serverIP, String username, String password) {      
        this.ip = serverIP;      
        this.username = username;      
        this.password = password;      
    }      
          
    public FtpUtil(String serverIP, int port, String username, String password) {      
        this.ip = serverIP;      
        this.username = username;      
        this.password = password;      
        this.port = port;      
    }      
     
    /**    
     * 连接ftp服务器    
     *     
     * @throws IOException    
     */     
    public void connectServer(){      
        ftpClient = new FtpClient();      
        try {      
            if(this.port != -1){      
                    ftpClient.openServer(this.ip,this.port);      
            }else{      
                ftpClient.openServer(this.ip);      
            }      
            ftpClient.login(this.username, this.password);      
            if (this.path.length() != 0){      
                ftpClient.cd(this.path);		// path是ftp服务下主目录的子目录                 
            }      
            ftpClient.binary();							// 用2进制上传、下载      
            logger.info("已登录到\"" + ftpClient.pwd() + "\"目录");      
        }catch (IOException e){      
            e.printStackTrace();      
            throw new RuntimeException("连接ftp服务器失败!",e);
        }      
    }      
          
    /**    
     * 断开与ftp服务器连接    
     *     
     * @throws IOException    
     */     
    public void closeServer(){      
        try{      
            if (is != null) {      
                is.close();      
            }      
            if (os != null) {      
                os.close();      
            }      
            if (ftpClient != null) {      
                ftpClient.closeServer();      
            }      
            logger.info("已从服务器断开");      
        }catch(IOException e){      
            e.printStackTrace();      
            throw new RuntimeException("断开与ftp服务器失败!",e);
        }      
    }      
          
    /**    
     * 检查文件夹在当前目录下是否存在    
     * @param dir    
     * @return    
     */     
     private boolean isDirExist(String dir){      
         String pwd = "";      
         try {      
             pwd = ftpClient.pwd();      
             ftpClient.cd(dir);      
             ftpClient.cd(pwd);      
         }catch(Exception e){      
             throw new RuntimeException("检查文件夹在当前目录下是否存在!",e);
         }      
         return true;       
     }      
          
    /**    
     * 在当前目录下创建文件夹    
     * @param dir    
     * @return    
     * @throws Exception    
     */     
     private void createDir(String dir){      
         try{      
            ftpClient.ascii();      
            StringTokenizer s = new StringTokenizer(dir, "/"); //sign      
            s.countTokens();      
            String pathName = ftpClient.pwd();      
            while(s.hasMoreElements()){      
                pathName = pathName + "/" + (String) s.nextElement();      
                ftpClient.sendServer("MKD " + pathName + "\r\n");      
                logger.info("创建文件夹"+pathName+"成功");
                ftpClient.readServerResponse();      
            } 
            ftpClient.binary();      
        }catch (IOException e){  
        	e.printStackTrace();
        	throw new RuntimeException("在当前目录下创建文件夹失败!",e); 
        }      
     }      
           
     /**    
      * ftp上传    
      * 如果服务器段已存在名为filename的文件夹，该文件夹中与要上传的文件夹中同名的文件将被替换    
      *     
      * @param filename 要上传的文件（或文件夹）名    
      * @return    
      * @throws Exception    
      */     
    public void upload(String filename){      
        String newname = "";      
        if(filename.indexOf("/") > -1){      
            newname = filename.substring(filename.lastIndexOf("/") + 1);      
        }else{      
            newname = filename;      
        }      
        upload(filename, newname);      
    }      
           
     /**    
      * ftp上传    
      * 如果服务器段已存在名为newName的文件夹，该文件夹中与要上传的文件夹中同名的文件将被替换    
      *     
      * @param fileName 要上传的文件（或文件夹）名    
      * @param newName 服务器段要生成的文件（或文件夹）名    
      * @return    
      */     
     public void upload(String fileName, String newName){      
         try{      
             String savefilename = new String(fileName.getBytes("ISO-8859-1"), "UTF-8");       
             File file_in = new File(savefilename);//打开本地待长传的文件      
             if(!file_in.exists()){      
                 throw new Exception("此文件或文件夹[" + file_in.getName() + "]有误或不存在!");      
             }      
             if(file_in.isDirectory()){      
                 upload(file_in.getPath(),newName,ftpClient.pwd());      
             }else{      
                 uploadFile(file_in.getPath(),newName);      
             }      
            
             logger.info("上传文件:"+fileName+"到"+newName+"成功!");
//             ftpClient.sendServer("chmod 644  " + newName + "\r\n");   
         }catch(Exception e){       
            throw new RuntimeException("ftp上传失败!",e); 
         }finally{      
             try{      
                 if(is != null){      
                     is.close();      
                 }      
                 if(os != null){       
                     os.close();       
                 }      
             }catch(IOException e){      
                 e.printStackTrace();      
            }       
         }      
     }      
           
     /**    
      * 真正用于上传的方法    
      * @param fileName    
      * @param newName    
      * @param path    
      * @throws Exception    
      */     
     private void upload(String fileName, String newName,String path) throws Exception{      
         String savefilename = new String(fileName.getBytes("ISO-8859-1"), "GBK");       
         File file_in = new File(savefilename);//打开本地待长传的文件      
         if(!file_in.exists()){      
             throw new Exception("此文件或文件夹[" + file_in.getName() + "]有误或不存在!");      
         }      
         if(file_in.isDirectory()){      
             if(!isDirExist(newName)){      
                 createDir(newName);      
             }      
             ftpClient.cd(newName);      
             File sourceFile[] = file_in.listFiles();      
             for(int i = 0; i < sourceFile.length; i++){      
                 if(!sourceFile[i].exists()){      
                     continue;      
                 }      
                 if(sourceFile[i].isDirectory()){      
                     this.upload(sourceFile[i].getPath(),sourceFile[i].getName(),path+"/"+newName);      
                 }else{      
                     this.uploadFile(sourceFile[i].getPath(),sourceFile[i].getName());      
                  }      
                }      
         }else{      
             uploadFile(file_in.getPath(),newName);      
         }      
         ftpClient.cd(path);      
     }      
     
    /**    
     *  upload 上传文件    
     *     
     * @param filename 要上传的文件名    
     * @param newname 上传后的新文件名    
     * @return -1 文件不存在 >=0 成功上传，返回文件的大小    
     * @throws Exception    
     */     
    public long uploadFile(String filename, String newname) throws Exception{      
        long result = 0;      
        TelnetOutputStream os = null;      
        FileInputStream is = null;      
        try {      
            File file_in = new File(filename);    
            if(!file_in.exists())      
                return -1;      
            os = ftpClient.put(newname);      
            result = file_in.length();      
            is = new FileInputStream(file_in);      
            byte[] bytes = new byte[1024];      
            int c;      
            while((c = is.read(bytes)) != -1){      
                os.write(bytes, 0, c);      
            }      
        }finally{      
            if(is != null){      
                is.close();      
            }      
            if(os != null){      
                os.close();      
            }      
        }      
        return result;      
    }      
     
    /**    
     * 从ftp下载文件到本地    
     *     
     * @param filename 服务器上的文件名    
     * @param newfilename 本地生成的文件名    
     * @return    
     * @throws Exception    
     */     
    public long downloadFile(String filename, String newfilename){      
        long result = 0;      
        TelnetInputStream is = null;      
        FileOutputStream os = null;      
        try{      
            is = ftpClient.get(filename);      
            File outfile = new File(newfilename);      
            os = new FileOutputStream(outfile);      
            byte[] bytes = new byte[1024];      
            int c;      
            while ((c = is.read(bytes)) != -1) {      
                os.write(bytes, 0, c);      
                result = result + c;      
            }      
        }catch (IOException e){      
            throw new RuntimeException("从ftp下载文件"+filename +"失败!");  
        }finally{      
            try {      
                if(is != null){      
                        is.close();      
                }      
                if(os != null){      
                    os.close();      
                }      
            } catch (IOException e) {      
                e.printStackTrace();      
            }      
        }      
        return result;      
    }      
     
    /**    
     * 取得相对于当前连接目录的某个目录下所有文件列表    
     *     
     * @param path    
     * @return    
     */     
    public List getFileList(String path){      
        List list = new ArrayList();      
        DataInputStream dis;      
        try {      
            dis = new DataInputStream(ftpClient.nameList(this.path + path));      
            String filename = "";      
            while((filename = dis.readLine()) != null){      
                list.add(filename);      
            }      
        } catch (IOException e) {      
            throw new RuntimeException("取得相对于当前连接目录的某个目录下所有文件列表  ");  
        }      
        return list;      
    }      
     
}  