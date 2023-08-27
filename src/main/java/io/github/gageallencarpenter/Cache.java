package io.github.gageallencarpenter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@Getter
@Setter
public class Cache<E> {
    private static final Logger logger = LoggerFactory.getLogger(Cache.class);
    private final DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
    private final ObjectMapper mapper = new ObjectMapper();
    private File folder;
    private File file;

    public Cache(File folder, File file){
        this.folder = folder;
        this.file = file;
        if(!isFolderPresent()){
            createFolder();
        }
        if(!isFilePresent()){
            createFile();
        }
    }

    public boolean add (E e){
        try{
            if(Files.size(getFilePath()) == 0) {
                mapper.writer(printer).writeValue(getFilePath().toFile(), List.of(e));
            } else if(Files.size(getFilePath()) > 0){
                Stream<E> appendedStream = Stream.concat(Arrays.stream(mapper.readValue(getFilePath().toFile(), new TypeReference<E[]>() {})), Stream.of(e));
                mapper.writer(printer).writeValue(getFilePath().toFile(), appendedStream.toArray());
            }
        } catch (IOException ex) {
            return false;
        }
        return true;
    }

    public List<E> get(TypeReference<List<E>> typeReference){
        try {
            if(isValidPath() && Files.size(getFilePath()) != 0){
                return mapper.readValue(getFilePath().toFile(), typeReference);
            }
        } catch (IOException ex) {
            logger.error(ex.getMessage());
        }
        return null;
    }

    public boolean remove(int index) {
        try {
            if (isValidPath() && Files.size(getFilePath()) != 0) {
                List<E> list = get(new TypeReference<>() {});
                if(index >= 0 && index < list.size()){
                    list.remove(index);
                    mapper.writer(printer).writeValue(getFilePath().toFile(), list);
                    return true;
                }else{
                    throw new IllegalArgumentException("Invalid index.");
                }
            }
        } catch (IOException ex) {
            logger.error(ex.getMessage());
        }
        return false;
    }

    public void removeAll() {
        if(isValidPath()){
            delete();
            createFile();
        }
    }

    public boolean isEmpty(TypeReference<List<E>> typeReference){
        List<E> data = get(typeReference);
        return data == null || data.isEmpty();
    }

    public boolean isFilePresent() {
        return Files.exists(file.toPath());
    }

    public boolean isFolderPresent() {
        return Files.exists(folder.toPath());
    }

    public boolean isValidPath(){
        return Files.exists(folder.toPath()) && Files.exists(file.toPath());
    }

    public Path getFilePath(){
        if(file == null){
            return null;
        };
        return file.toPath();
    }

    public String getFileName(){
        if(file == null){
            return null;
        }
        return file.getName();
    }

    public Path getFolderPath(){
        if(folder == null){
            return null;
        }
        return folder.toPath();
    }

    public String getFolderName(){
        if(folder == null){
            return null;
        }
        return folder.getName();
    }

    public boolean createFile(){
        if (!isFilePresent()) {
            try {
                return file.createNewFile();
            } catch (Exception ex) {
                logger.error(ex.getMessage());
            }
        }
        return false;
    }

    public boolean createFolder(){
        if (!isFolderPresent()) {
            try {
                return folder.mkdirs();
            } catch (Exception ex) {
                logger.error(ex.getMessage());
            }
        }
        return false;
    }

    public boolean delete(){
        if (isFilePresent()) {
            try {
                return file.delete();
            } catch (Exception ex) {
                logger.error(ex.getMessage());
            }
        }
        return false;
    }
}
