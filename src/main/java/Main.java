import com.dropbox.core.DbxDownloader;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import com.dropbox.core.v2.users.FullAccount;

import java.io.File;
import java.io.FileInputStream;

import java.io.FileOutputStream;
import java.io.InputStream;


public class Main {

    private DbxClientV2 client;

    public Main(String ACCESS_TOKEN){
        DbxRequestConfig config = new DbxRequestConfig("esdras", "en_US");
        client = new DbxClientV2(config, ACCESS_TOKEN);
    }

    public static void main(String args[])  throws DbxException {
        Main m = new Main("boAJFLqL_LAAAAAAAAAFhMXiyOAAWJqtXyPYmtVc-tzK2gY-V1A1J0ZJ4ocqRYg0");

        FullAccount account = m.client.users().getCurrentAccount();
        System.out.println(account.getName().getDisplayName());


        // envia do pc para o dropbox
        m.sendFile(new File("/home/josafa/teste.rb"));

        // Puxa todos os arquivos do diretório esdras
        m.readFiles();
    }

    public void sendFile(File file){
        try  {
            InputStream in = new FileInputStream(file);
            FileMetadata metadata = client.files().uploadBuilder("/esdras/" + file.getName())
                    .uploadAndFinish(in);
        } catch (Exception e){

        }
    }


    public void readFiles(){

        try{
            ListFolderResult result = client.files().listFolder("/esdras/");
            while (true) {
                for (Metadata metadata : result.getEntries()) {
                    System.out.println(metadata.getPathLower());
                    DbxDownloader<FileMetadata> downloader = client.files().download(metadata.getPathLower());

                    FileOutputStream out = new FileOutputStream("diretorio onde tu quer jogar no teu pc" +metadata.getName());
                    downloader.download(out);
                    out.close();
                }

                if (!result.getHasMore()) {
                    break;
                }

                result = client.files().listFolderContinue(result.getCursor());
            }

        } catch (Exception e){

        }

    }

}

