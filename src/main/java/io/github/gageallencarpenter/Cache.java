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

/**
 * A generic cache class for storing and managing data in files. Any object can
 * be stored in the cache as long as it is serializable.
 *
 * @param <E> The type of elements to store in the cache.
 */
@Getter
@Setter
public class Cache<E> {
    private static final Logger logger = LoggerFactory.getLogger(Cache.class);
    private final DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
    private final ObjectMapper mapper = new ObjectMapper();
    private File folder;
    private File file;

    /**
     * Constructs a Cache instance with the specified folder and file.
     *
     * @param folder The folder where the cache file will be stored.
     * @param file   The cache file.
     */
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

    /**
     * Adds an element to the cache. If the cache file is empty, the element is
     * added to the file. If the cache file is not empty, the element is added
     * to the end of the file. If the cache file does not exist, it is created.
     *
     * @param e The element to add.
     * @return True if the element was successfully added, false otherwise.
     */
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

    /**
     * If the cache file exists and is not empty, return the contents of the
     * cache file as a list of the specified type.
     *
     * @param typeReference This is a Jackson class that allows you to specify
     *                      the type of the object you want to deserialize.
     * @return A list of objects of type E.
     */
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

    /**
     * Removes an element from the cache at the specified index.
     *
     * @param index The index of the element to remove.
     * @return True if the element was successfully removed, false otherwise.
     */
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

    /**
     * Removes all elements from the cache including the empty array and
     * creates an empty cache file. This method ensures that the cache
     * file is empty by deleting it and then creating a new cache file with
     * the same name.
     */
    public void removeAll() {
        if(isValidPath()){
            delete();
            createFile();
        }
    }

    /**
     * Returns true if the cache file is empty, false otherwise.
     *
     * @param typeReference This is a Jackson class that allows you to specify
     * @return True if the cache file is empty, false otherwise.
     */
    public boolean isEmpty(TypeReference<List<E>> typeReference){
        List<E> data = get(typeReference);
        return data == null || data.isEmpty();
    }

    /**
     * Returns true if the cache file exists, false otherwise.
     *
     * @return True if the cache file exists, false otherwise.
     */
    public boolean isFilePresent() {
        return Files.exists(file.toPath());
    }

    /**
     * Returns true if the cache folder exists, false otherwise.
     *
     * @return True if the cache folder exists, false otherwise.
     */
    public boolean isFolderPresent() {
        return Files.exists(folder.toPath());
    }

    /**
     * Returns true if the cache folder and file exist, false otherwise.
     *
     * @return True if the cache folder and file exist, false otherwise.
     */
    public boolean isValidPath(){
        return Files.exists(folder.toPath()) && Files.exists(file.toPath());
    }

    /**
     * Obtains the path of the cache file.
     *
     * @return The path of the cache file.
     */
    public Path getFilePath(){
        if(file == null){
            return null;
        };
        return file.toPath();
    }

    /**
     * Obtains the name of the cache file.
     *
     * @return The name of the cache file.
     */
    public String getFileName(){
        if(file == null){
            return null;
        }
        return file.getName();
    }

    /**
     * Obtains the path of the cache folder.
     *
     * @return The path of the cache folder.
     */
    public Path getFolderPath(){
        if(folder == null){
            return null;
        }
        return folder.toPath();
    }

    /**
     * Obtains the name of the cache folder.
     *
     * @return The name of the cache folder.
     */
    public String getFolderName(){
        if(folder == null){
            return null;
        }
        return folder.getName();
    }

    /**
     * Creates a new cache file.
     *
     * @return True if the file was created, false otherwise.
     */
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

    /**
     * Creates a new cache folder.
     *
     * @return True if the folder was created, false otherwise.
     */
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

    /**
     * Deletes the cache file.
     *
     * @return True if the file was deleted, false otherwise.
     */
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
