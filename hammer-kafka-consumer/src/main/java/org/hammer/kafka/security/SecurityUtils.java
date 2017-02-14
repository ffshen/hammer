package org.hammer.kafka.security;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SecurityUtils { 

    private static Logger logger = LoggerFactory.getLogger(SecurityUtils.class);  
    
    /**
     * The jass.conf for zookeeper client security login.
     */
    public static final String ZOOKEEPER_AUTH_JASSCONF = "java.security.auth.login.config";
    
    /**
     * Zookeeper quorum principal.
     */
    public static final String ZOOKEEPER_AUTH_PRINCIPAL = "zookeeper.server.principal";
    
    /**
     * java security krb5 file path
     */
    public static final String JAVA_SECURITY_KRB5_CONF = "java.security.krb5.conf";
    
    /**
     * line operator string
     */
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
    
    private static final String KAFKA_JAAS_POSTFIX = ".kafka.jaas.conf";
    
    /**
     * 用户自己申请的机机账号keytab文件名称
     */
    private static final String USER_KEYTAB_FILE = "user.keytab";
    
    /**
     * 用户自己申请的机机账号名称
     */
    private static final String USER_PRINCIPAL = "TDR_TEST";
    
    
    public static void securityPrepare(String principal ,String jaasFile,String userKeyTableFile,String krbFile) throws IOException {
    	
        if (!isFileExists(jaasFile))
        {
            throw new IOException(jaasFile + " is not exist.");
        }
        
        if (!isFileExists(userKeyTableFile))
        {
            throw new IOException(userKeyTableFile + " is not exist.");
        }
        
        if (!isFileExists(krbFile))
        {
            throw new IOException(krbFile + " is not exist.");
        }    	
        System.setProperty(ZOOKEEPER_AUTH_PRINCIPAL, principal );
        System.setProperty(ZOOKEEPER_AUTH_JASSCONF, jaasFile);
        System.setProperty(JAVA_SECURITY_KRB5_CONF, krbFile);
    }
    
    public static Boolean isSecurityModel()
    {
        Boolean isSecurity = true ;
        String krbFilePath = System.getProperty("user.dir") + File.separator + "conf" + File.separator + "kafkaSecurityMode";
        Properties securityProps = new Properties();
        
        // file does not exist.
        if (!isFileExists(krbFilePath))
        {
            return isSecurity;
        }
        
        try 
        {
            securityProps.load(new FileInputStream(krbFilePath));
            if ("yes".equalsIgnoreCase(securityProps.getProperty("kafka.client.security.mode")))
            {
                isSecurity = true;
            }
        }
        catch (Exception e)
        {
        	logger.error(" SecurityUtils isSecurityModel : ",e);
        }
        
        return isSecurity;
    }
    
    public static void securityPrepare() throws IOException
    {
        String filePath = System.getProperty("user.dir") +  File.separator + "conf" + File.separator;
        
        String jaasFile = filePath + System.getProperty("user.name") + KAFKA_JAAS_POSTFIX;
        
        String krbFile = filePath + "krb5.conf";
        
        String userKeyTableFile = filePath + USER_KEYTAB_FILE;
        //windows路径下分隔符替换
        jaasFile = jaasFile.replace("\\", "\\\\");
        userKeyTableFile = userKeyTableFile.replace("\\", "\\\\");
        
        //删除jaas文件
        deleteJaasFile(jaasFile);
        writeJaasFile(jaasFile, userKeyTableFile, USER_PRINCIPAL);
        
        if (!isFileExists(jaasFile))
        {
            throw new IOException(jaasFile + " is not exist.");
        }
        
        if (!isFileExists(userKeyTableFile))
        {
            throw new IOException(userKeyTableFile + " is not exist.");
        }
        
        if (!isFileExists(krbFile))
        {
            throw new IOException(krbFile + " is not exist.");
        }
        
        System.setProperty(ZOOKEEPER_AUTH_PRINCIPAL, "zookeeper/hadoop.hadoop.com" );
        System.setProperty(ZOOKEEPER_AUTH_JASSCONF, jaasFile);
        System.setProperty(JAVA_SECURITY_KRB5_CONF, krbFile);
    }
    
    private static void deleteJaasFile(String jaasPath) throws IOException
    {
        File jaasFile = new File(jaasPath);
        if (jaasFile.exists())
        {
            if (!jaasFile.delete())
            {
                throw new IOException("Failed to delete exists jaas file.");
            }
        }
    }
    
    /**
     * 写入jaas文件
     *
     * @throws IOException 写文件异常
     */
    private static void writeJaasFile(String jaasPath, String keyTabPath, String userPrincipal) throws IOException
    {
        FileWriter fw = new FileWriter(jaasPath);
        
        try
        {
            fw.write(createKeyTabContext(keyTabPath, userPrincipal));
        }
        catch (IOException e)
        {
            throw new IOException("Failed to create jaas.conf File");
        }
        finally
        {
            fw.close();
        }
    }
    
    /*
     * 创建机机账户登陆的jaas文件内容
     */
    private static String createKeyTabContext(String keyTabPath, String userPrincipal)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("Client {").append(LINE_SEPARATOR);
        sb.append("com.sun.security.auth.module.Krb5LoginModule required").append(LINE_SEPARATOR);
        sb.append("useKeyTab=true").append(LINE_SEPARATOR);
        sb.append("keyTab=\"" + keyTabPath + "\"").append(LINE_SEPARATOR);
        sb.append("principal=\"" + userPrincipal + "\"").append(LINE_SEPARATOR);
        sb.append("useTicketCache=false").append(LINE_SEPARATOR);
        sb.append("storeKey=true").append(LINE_SEPARATOR);
        sb.append("debug=false;").append(LINE_SEPARATOR);
        sb.append("};").append(LINE_SEPARATOR);
        return sb.toString();
    }
    
    /*
     * 判断文件是否存在
     */
    private static boolean isFileExists(String fileName)
    {
        File file = new File(fileName);
        
        return file.exists();
    }
}
