package com.david.copyright.util;

import io.ipfs.api.IPFS;

import io.ipfs.api.IPFS;
import io.ipfs.api.MerkleNode;
import io.ipfs.api.NamedStreamable;
import io.ipfs.multihash.Multihash;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class IpfsUtil {
    static IPFS ipfs = new IPFS("/ip4/127.0.0.1/tcp/5001");

    public static String upload(String filePathName) throws IOException {
        //filePathName指的是文件的上传路径+文件名，如D:/1.png
        NamedStreamable.FileWrapper file = new NamedStreamable.FileWrapper(new File(filePathName));
        MerkleNode addResult = ipfs.add(file).get(0);
        String hash=addResult.hash.toString();
        String workUri="https://ipfs.io/ipfs/"+hash;
        return  workUri;
    }

    public static void download(String filePathName, String hash) throws IOException {
        Multihash filePointer = Multihash.fromBase58(hash);
        byte[] data = ipfs.cat(filePointer);
        if (data != null) {
            File file = new File(filePathName);
            if (file.exists()) {
                file.delete();
            }
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(data, 0, data.length);
            fos.flush();
            fos.close();
        }
    }

    public static void main(String[] args) throws IOException {
        System.out.println(upload("C:/Users/David/Desktop/2.txt"));;
    }
}
