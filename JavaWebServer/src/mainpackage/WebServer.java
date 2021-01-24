package mainpackage;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * classe per la gestione delle connessioni
 * @author Francesco Lizzio
 */

public class WebServer implements Runnable{

    static final String DEFAULT_FILE = "index.html";
    static final String FILE_NOT_FOUND = "404.html";
    static final String METHOD_NOT_SUPPORTED = "not_supported.html";
    static final String FILE_MOVED="301.html";
    static final File FILE = new File("");
    static final String PATH = FILE.getAbsolutePath();
    static final File WEB_ROOT = new File(PATH);
    static final int PORT = 8080;
    static final boolean VERBOSE = true;
    private Socket clientSocket;

    public WebServer(Socket socket) {
        clientSocket = socket;
    }

    public static void main(String[] args) {
            try {
                ServerSocket serverConnect = new ServerSocket(PORT);
                System.out.println("Server started.\nListening for connections on port : " + PORT + " ...\n");
                while (true) {
                    WebServer myServer = new WebServer(serverConnect.accept());
                    if (VERBOSE) {
                        System.out.println("Connecton opened. (" + new Date() + ")");
                    }
                    Thread thread = new Thread(myServer);
                    thread.start();
                }
            }
            catch (IOException e) {
                System.err.println("Server Connection error : " + e.getMessage());
            }
    }

    @Override
    public void run() {
        BufferedReader bufferedInput = null;
        PrintWriter output = null;
        BufferedOutputStream dataOut = null;
        String fileRequested = null;
        try {
            bufferedInput = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            output = new PrintWriter(clientSocket.getOutputStream());
            dataOut = new BufferedOutputStream(clientSocket.getOutputStream());
            String input = bufferedInput.readLine();
            StringTokenizer parse = new StringTokenizer(input);
            String method = parse.nextToken().toUpperCase();
            fileRequested = parse.nextToken().toLowerCase();
            if (!method.equals("GET")  &&  !method.equals("HEAD")) {
                if (VERBOSE) {
                    System.out.println("501 Not Implemented : " + method + " method.");
                }
                File file = new File(WEB_ROOT, METHOD_NOT_SUPPORTED);
                int fileLength = (int) file.length();
                String contentMimeType = "text/html";
                byte[] fileData = readFileData(file, fileLength);
                output.println("HTTP/1.1 501 Not Implemented");
                output.println("Server: Java HTTP Server from SSaurel : 1.0");
                output.println("Date: " + new Date());
                output.println("Content-type: " + contentMimeType);
                output.println("Content-length: " + fileLength);
                output.println();
                output.flush();
                dataOut.write(fileData, 0, fileLength);
                dataOut.flush();
            }
            else {
                if (fileRequested.endsWith("/")) {
                    fileRequested += DEFAULT_FILE;
                }
                File file;
                if(fileRequested.equals("/punti-vendita.xml")){
                    file=fromJSONToXML();
                }
                else if(fileRequested.equals("/db/xml")||fileRequested.equals("/db/json")){
                    List list = retrieveList();
                    if(fileRequested.endsWith("xml")){
                        file=classToXML(list);
                        fileRequested+="elenco.xml";
                    }
                    else{
                        file=classToJSON(list);
                        fileRequested+="elenco.json";
                    }
                }
                else{
                    file = new File(WEB_ROOT, fileRequested);
                }
                int fileLength = (int) file.length();
                String content = getContentType(fileRequested);
                if (method.equals("GET")) {
                    byte[] fileData = readFileData(file, fileLength);
                    output.println("HTTP/1.1 200 OK");
                    output.println("Server: Java HTTP Server from SSaurel : 1.0");
                    output.println("Date: " + new Date());
                    output.println("Content-type: " + content);
                    output.println("Content-length: " + fileLength);
                    output.println();
                    output.flush(); 
                    dataOut.write(fileData, 0, fileLength);
                    dataOut.flush();
                }
                if (VERBOSE) {
                    System.out.println("File " + fileRequested + " of type " + content + " returned");
                }
            }
        }
        catch (FileNotFoundException fnfe) {
            try {
                String[] path=fileRequested.split("/");
                int pathNum=path.length;
                String oggetto=path[pathNum-1];
                if(oggetto.lastIndexOf(".")==-1){
                    redirect(output,dataOut,fileRequested+"/");
                }
                else{
                    fileNotFound(output, dataOut, fileRequested);
                }
            } catch (IOException ioe) {
                System.err.println("Error with file not found exception : " + ioe.getMessage());
            }
        }
        catch (IOException ioe) {
            System.err.println("Server error : " + ioe);
        } catch (ClassNotFoundException | SQLException ex) {
            System.err.println("Errore nel recupero dei dati dal DB. "+ex.getLocalizedMessage());
        }
        finally {
            try {
                bufferedInput.close();
                output.close();
                dataOut.close();
                clientSocket.close(); 
            } catch (IOException e) {
                System.err.println("Error closing stream : " + e.getMessage());
            } 
            if (VERBOSE) {
                System.out.println("Connection closed.\n");
            }
        }
    }

    private List retrieveList() throws ClassNotFoundException, SQLException{
        ArrayList<NameList> names=new ArrayList<>();
        ResultSet res=new MySQL().startQuery("select nome, cognome from persone");
        while (res.next()) {
            names.add(new NameList(res.getString(1), res.getString(2)));
        }
        return new List(names);
    }

    private File classToXML(List list) throws IOException{
        XmlMapper xmlMapper = new XmlMapper();
        File fXML=new File(WEB_ROOT+"\\elenco.xml");
        if(!fXML.exists()){
            fXML.createNewFile();
        }
        xmlMapper.writeValue(fXML, list);
        return fXML;
    }

    private File classToJSON(List list) throws IOException{
        ObjectMapper objectMapper = new ObjectMapper();
        File fJSON=new File(WEB_ROOT+"\\elenco.json");
        if(!fJSON.exists()){
            fJSON.createNewFile();
        }
        objectMapper.writeValue(fJSON, list);
        return fJSON;
    }

    private File fromJSONToXML() throws FileNotFoundException, IOException{
        File fileJSON=new File(PATH+"\\puntiVendita.json");
        String string=readFile(fileJSON);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.USE_JAVA_ARRAY_FOR_JSON_ARRAY, true);
        ResultList list = objectMapper.readValue(string, ResultList.class);
        XmlMapper xmlMapper = new XmlMapper();
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        xmlMapper.writeValue(byteArray, list); 
        String arrayXML=byteArray.toString();
        System.out.println(arrayXML);
        File fileXML=new File(WEB_ROOT+"\\punti-vendita.xml");
        if(fileXML.exists()){
            fileXML.delete();
        }
        fileXML.createNewFile();
        FileWriter fw=new FileWriter(fileXML);
        fw.write(arrayXML);
        fw.close();
        return fileXML;
    }

    private String readFile(File file) throws FileNotFoundException, IOException{
        String string="";
        FileReader filereader=new FileReader(file);
        BufferedReader bufferedReader=new BufferedReader(filereader);
        for(;;){
            String read=bufferedReader.readLine();
            if(read==null){
                break;
            }
            string+=read;
        }
        return string;
    }

    private byte[] readFileData(File file, int fileLength) throws IOException {
        FileInputStream fileIn = null;
        byte[] fileData = new byte[fileLength];
        try {
            fileIn = new FileInputStream(file);
            fileIn.read(fileData);
        }
        finally {
            if (fileIn != null){
                fileIn.close();
            }
        }
        return fileData;
    }

    private String getContentType(String fileRequested) {
        String type=fileRequested.substring(fileRequested.lastIndexOf(".")+1);
        switch(type){
            case "html":
                return "text/html";
            case "xml":
                return "application/xml";
            case "json":
                return "application/json";
            default:
                return "text/plain";
        }
    }

    private void fileNotFound(PrintWriter out, OutputStream dataOut, String fileRequested) throws IOException {
        File file = new File(WEB_ROOT, FILE_NOT_FOUND);
        int fileLength = (int) file.length();
        String content = "text/html";
        byte[] fileData = readFileData(file, fileLength);
        out.println("HTTP/1.1 404 File Not Found");
        out.println("Server: Java HTTP Server from SSaurel : 1.0");
        out.println("Date: " + new Date());
        out.println("Content-type: " + content);
        out.println("Content-length: " + fileLength);
        out.println();
        out.flush();
        dataOut.write(fileData, 0, fileLength);
        dataOut.flush();
        if (VERBOSE) {
            System.out.println("File " + fileRequested + " not found");
        }
    }

    private void redirect(PrintWriter out, OutputStream dataOut,String directoryRequested) throws IOException{
        File file = new File(WEB_ROOT, FILE_MOVED);
        int fileLength = (int) file.length();
        String content = "text/html";
        byte[] fileData = readFileData(file, fileLength);
        out.println("HTTP/1.1 301 Moved Permanently");
        out.println("Server: Java HTTP Server from SSaurel : 1.0");
        out.println("Date: " + new Date());
        out.println("Content-type: " + content);
        out.println("Content-length: " + fileLength);
        out.println("Location: "+directoryRequested);
        out.println();
        out.flush();
        dataOut.write(fileData, 0, fileLength);
        dataOut.flush();
        if (VERBOSE) {
            System.out.println("Directory " + directoryRequested + " hint sended");
        }
    }
}