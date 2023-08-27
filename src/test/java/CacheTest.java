import com.fasterxml.jackson.core.type.TypeReference;
import io.github.gageallencarpenter.Cache;
import org.junit.*;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;

public class CacheTest {

    @Rule
    public TemporaryFolder tempFolder;
    private Cache<String> cache;

    @Before
    public void setUp(){
        testConstructor();
    }

    @After
    public void tearDown(){
        tempFolder.delete();
    }

    @Test
    public void testConstructor(){
        try {
            tempFolder = new TemporaryFolder();
            tempFolder.create();
            File folder = tempFolder.newFolder("folder");
            File file = tempFolder.newFile("file");
            cache = new Cache<>(folder, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAddAndGet(){
        //Test empty file
        Assert.assertNull(cache.get(new TypeReference<>(){}));

        //Test one element
        Assert.assertTrue(cache.add("test"));
        Assert.assertEquals("test", cache.get(new TypeReference<>(){}).get(0));
        Assert.assertEquals(1, cache.get(new TypeReference<>(){}).size());

        //Test multiple elements
        Assert.assertTrue(cache.add("test2"));
        Assert.assertEquals("test", cache.get(new TypeReference<>(){}).get(0));
        Assert.assertEquals("test2", cache.get(new TypeReference<>(){}).get(1));
        Assert.assertEquals(2, cache.get(new TypeReference<>(){}).size());
    }

    @Test
    public void testAddRemoveAndGet(){
        //Test empty file
        Assert.assertNull(cache.get(new TypeReference<>(){}));
        Assert.assertFalse(cache.remove(1));

        //Test one element
        Assert.assertTrue(cache.add("test"));
        Assert.assertEquals("test", cache.get(new TypeReference<>(){}).get(0));
        Assert.assertEquals(1, cache.get(new TypeReference<>(){}).size());
        Assert.assertTrue(cache.remove(0));

        //Test multiple elements
        Assert.assertTrue(cache.add("test"));
        Assert.assertTrue(cache.add("test2"));
        Assert.assertEquals("test", cache.get(new TypeReference<>(){}).get(0));
        Assert.assertEquals("test2", cache.get(new TypeReference<>(){}).get(1));
        Assert.assertEquals(2, cache.get(new TypeReference<>(){}).size());
        Assert.assertTrue(cache.remove(0));
        Assert.assertEquals("test2", cache.get(new TypeReference<>(){}).get(0));
        Assert.assertEquals(1, cache.get(new TypeReference<>(){}).size());
        Assert.assertTrue(cache.remove(0));
    }

    @Test
    public void testAddGetAndRemoveAll(){
        //Test empty file
        Assert.assertNull(cache.get(new TypeReference<>(){}));
        cache.removeAll();
        Assert.assertNull(cache.get(new TypeReference<>(){}));

        //Test one element
        Assert.assertTrue(cache.add("test"));
        Assert.assertEquals("test", cache.get(new TypeReference<>(){}).get(0));
        Assert.assertEquals(1, cache.get(new TypeReference<>(){}).size());
        cache.removeAll();
        Assert.assertNull(cache.get(new TypeReference<>(){}));

        //Test multiple elements
        Assert.assertTrue(cache.add("test"));
        Assert.assertTrue(cache.add("test2"));
        Assert.assertEquals("test", cache.get(new TypeReference<>(){}).get(0));
        Assert.assertEquals("test2", cache.get(new TypeReference<>(){}).get(1));
        Assert.assertEquals(2, cache.get(new TypeReference<>(){}).size());
        cache.removeAll();
        Assert.assertNull(cache.get(new TypeReference<>(){}));
    }

    @Test
    public void testIsEmpty(){
        //Test empty file
        Assert.assertTrue(cache.isEmpty(new TypeReference<>(){}));

        //Test one element
        Assert.assertTrue(cache.add("test"));
        Assert.assertFalse(cache.isEmpty(new TypeReference<>(){}));
        cache.remove(0);
        Assert.assertTrue(cache.isEmpty(new TypeReference<>(){}));

        //Test multiple elements
        Assert.assertTrue(cache.add("test"));
        Assert.assertTrue(cache.add("test2"));
        Assert.assertFalse(cache.isEmpty(new TypeReference<>(){}));
        cache.remove(0);
        Assert.assertFalse(cache.isEmpty(new TypeReference<>(){}));
        cache.remove(0);
        Assert.assertTrue(cache.isEmpty(new TypeReference<>(){}));
    }

    @Test
    public void testIsFilePresent(){
        //Test true condition
        Assert.assertTrue(cache.isFilePresent());

        //Test false condition
        Assert.assertTrue(cache.getFilePath().toFile().delete());
        Assert.assertFalse(cache.isFilePresent());
    }

    @Test
    public void testIsFolderPresent(){
        //Test true condition
        Assert.assertTrue(cache.isFolderPresent());

        //Test false condition
        Assert.assertTrue(cache.getFolderPath().toFile().delete());
        Assert.assertFalse(cache.isFolderPresent());
    }

    @Test
    public void testIsValidPath(){
        //Test true condition
        Assert.assertTrue(cache.isValidPath());

        //Test false condition 1 where file is deleted
        Assert.assertTrue(cache.getFilePath().toFile().delete());
        Assert.assertFalse(cache.isValidPath());

        //Test false condition 2 where folder is deleted
        Assert.assertTrue(cache.getFolderPath().toFile().delete());
        Assert.assertFalse(cache.isValidPath());
    }

    @Test
    public void testGetFilePath(){
        //test null condition
        cache.setFile(null);
        Assert.assertNull(cache.getFilePath());

        //test non null condition
        cache.setFile(tempFolder.getRoot());
        Assert.assertEquals(tempFolder.getRoot().toPath(), cache.getFilePath());
    }

    @Test
    public void testGetFileName(){
        //test null condition
        cache.setFile(null);
        Assert.assertNull(cache.getFileName());

        //test non null condition
        cache.setFile(tempFolder.getRoot());
        Assert.assertEquals(tempFolder.getRoot().getName(), cache.getFileName());
    }

    @Test
    public void testGetFolderPath(){
        //test null condition
        cache.setFolder(null);
        Assert.assertNull(cache.getFolderPath());

        //test non null condition
        cache.setFolder(tempFolder.getRoot());
        Assert.assertEquals(tempFolder.getRoot().toPath(), cache.getFolderPath());
    }

    @Test
    public void testGetFolderName(){
        //test null condition
        cache.setFolder(null);
        Assert.assertNull(cache.getFolderName());

        //test non null condition
        cache.setFolder(tempFolder.getRoot());
        Assert.assertEquals(tempFolder.getRoot().getName(), cache.getFolderName());
    }

    @Test
    public void testCreateFile(){
        //test false condition of existing file
        Assert.assertFalse(cache.createFile());

        //test true condition of non-existing file
        Assert.assertTrue(cache.getFilePath().toFile().delete());
        Assert.assertTrue(cache.createFile());
    }

    @Test
    public void testCreateFolder(){
        //test false condition of existing folder
        Assert.assertFalse(cache.createFolder());

        //test true condition of non-existing folder
        Assert.assertTrue(cache.getFolderPath().toFile().delete());
        Assert.assertTrue(cache.createFolder());
    }

    @Test
    public void testDelete(){
        //test false condition of non-existing file
        Assert.assertTrue(cache.getFilePath().toFile().delete());
        Assert.assertFalse(cache.delete());

        //test true condition of existing file
        Assert.assertTrue(cache.createFile());
        Assert.assertTrue(cache.delete());
    }
}
