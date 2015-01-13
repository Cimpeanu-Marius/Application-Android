package com.example.marius.myapplication;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by marius on 11/18/2014.
 */
public class RWServices extends FragmentActivity {

    BufferedReader _bufferedReader;
    BufferedWriter _bufferedWriter;
    Encryption crypto = new Encryption();
    protected String encryption_pass = "100A58710020058700D7E";

    public void CreateFileWriter(String fileName) throws IOException {
        String _path = getBaseContext().getFilesDir().getAbsolutePath();
        File _file = new File(_path + File.separator  + fileName);
        String file_name = _file.getName();
        FileOutputStream fileos = openFileOutput(file_name, Context.MODE_APPEND);
        DataOutputStream out = new DataOutputStream(fileos);
        _bufferedWriter = new BufferedWriter(new OutputStreamWriter(out));
    }

    public void CreateFileReader(String fileName) throws FileNotFoundException {
        String _path = getBaseContext().getFilesDir().getAbsolutePath();
        File _file = new File(_path + File.separator  + fileName);

       if(_file.exists() || _file.isFile()){
           if(_bufferedReader == null){
            try {
                FileInputStream fstream = openFileInput(_file.getName());
                DataInputStream in = new DataInputStream(fstream);
                _bufferedReader = new BufferedReader(new InputStreamReader(in));
            } catch (IOException e) {
                e.printStackTrace();
                }
            }
       }

    }

    public void DeleteFile(String fileName)
    {
        String _path = getBaseContext().getFilesDir().getAbsolutePath();
        File _file = new File(_path + File.separator  + fileName);
        if(_file.exists() && _file.isFile())
        _file.delete();
    }

    public void WriteLine(String str1, String str2, String fileName) throws Exception {
        CreateFileWriter(fileName);
        String user_passwordCombination = str1+"#"+ crypto.encrypt(encryption_pass,str2);
        _bufferedWriter.append(user_passwordCombination);
        CloseBufferedWriter();

    }

    public Boolean CheckUsernameExistance(String username) throws IOException {
        String line;
        Boolean exist = false;
        CreateFileReader("Authentification.txt");
        while((line = _bufferedReader.readLine()) != null )
        {
            StringTokenizer st = new StringTokenizer(line,"#");
            if(st.countTokens() != 0 && st.nextElement().equals(username))
            {
                exist = true;
                break;
            }
        }

        CloseBufferedReader();

        return exist;
    }

    public Boolean CheckCredentialsValidity(String username, String introduced_password) throws Exception {
        String line;
        Boolean grantAccess = false;
        CreateFileReader("Authentification.txt");
         while((line = _bufferedReader.readLine()) != null )
        {
            StringTokenizer st = new StringTokenizer(line,"#");
            if(st.countTokens()>1){
            if(st.nextElement().equals(username)){
                String encrypted_password = (String) st.nextElement();
                String decrypted_password = crypto.decrypt(encryption_pass,encrypted_password);
                if(decrypted_password.equals(introduced_password)) {
                    grantAccess = true;
                    break;
                }
            }
        }
        }
        CloseBufferedReader();

        return grantAccess;
    }

    public void CloseBufferedReader() throws  IOException {
        _bufferedReader.close();
        _bufferedReader = null;
    }

    public void CloseBufferedWriter() throws  IOException {
        _bufferedWriter.flush();
        _bufferedWriter.close();
        _bufferedWriter = null;
    }

    public Boolean VerifyFileExistance(String fileName) throws  IOException {
        String path = getBaseContext().getFilesDir().getAbsolutePath();
        File authentification = new File(path + File.separator + fileName);

        if(authentification.exists())
            return true;
        else
            return false;
    }

    public ArrayList<String> GetLocationsList(String fileName) throws  IOException {
        String line;
        CreateFileReader(fileName);
        ArrayList<String> locationsList = new ArrayList<String>();
        while((line = _bufferedReader.readLine()) != null )
        {
           locationsList.add(line + "");
        }
        CloseBufferedReader();
        return locationsList;
    }

    public ArrayList<String> getWordsFromExpression(String locationExpression){
        ArrayList<String> words = new ArrayList<String>();
        StringTokenizer tokens = new StringTokenizer(locationExpression,"#");
        if(tokens.countTokens() > 0) {
            while (tokens.hasMoreElements()) {
                words.add(tokens.nextToken());
            }
        }
        return words;
    }


}