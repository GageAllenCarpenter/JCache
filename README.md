
# JCache

JCache is a generic utility for storing and managing data in files. JCache is similar to other data structures in the Java Collections Framework (JCF), such as ArrayList, but utilizes the computers secondary storage rather than primary storage.

## Table of Contents

- [Introduction](#introduction)
- [Maven](#maven)
- [Features](#features)
- [Usage](#usage)
- [Overview](#overview)
- [Examples](#examples)
- [Contributions](#contributions)
- [License](#license)

## Introduction
JCache is a generic utility for storing and managing data in files that is compatible with any serializable Java object. It functions similarly to other data structures in the Java Collections Framework (JCF), such as ArrayList, but with a focus on utilizing secondary storage rather than primary memory.

Traditional data structures within the JCF operate in memory, which means they lack data persistence. In most cases, databases are used to provide data persistence. However, there are scenarios where a database might not be a suitable option. This is where JCache comes in. It offers familiar methods like `add`, `get`, and `remove`, similar to those found in the collections framework. JCache takes these methods and stores data into the secondary storage of the computer, such as the hard drive, allowing stored data to be retrieved at a later time.

Additionally, JCache provides the advantage of enhanced security. No connection strings are associated with JCache, and the data is stored locally. This design ensures that access to the data is only possible for those who possess the JSON output file associated with JCache on the local machine.
## Maven

To use JCache in your Maven project add this dependency to the dependencies section of the pom.xml file within your project.
```mvn 
Currently working with SonaType(Maven Host) on deployment 8/27/2023
```
## Features
- Store and manage data persistently in files.
- Familiar methods like `add`, `get`, and `remove`.
- No reliance on external databases.
- Enhanced security with local storage.
## Usage
1. Add the Cache class or the Maven dependency from this repository to your project.
2. Utilize the available methods within Cache.java to interact with your cache.
## Overview
|Method|Use Case  |
|--|--|
|add(E e)  | Add an element to the cache. |
| get(TypeReference<List<E>> typeReference) | Retrieve cached data of a specific type. |
| remove(int index) | Remove an element from the cache. |
| removeAll() | Remove all elements from the cache. |
| isEmpty(TypeReference<List<E>> typeReference) | Check if the cache is empty for a specific type. |
| isFilePresent() | Check if the cache file exists. |
| isFolderPresent() | Check if the cache folder exists. |
| isValidPath() | Check if both folder and file exist. |
| getFilePath() | Get the path to the cache file. |
| getFileName() | Get the name of the cache file. |
| getFolderPath() | Get the path to the cache folder. |
| getFolderName() | Get the name of the cache folder. |
| createFile() | Create the cache file if not present. |
| createFolder() | Create the cache folder if not present. |
| delete() | Delete the cache file if present. |

## Examples
Below are a few core examples that demonstrate how to use JCache to store and manage data in files.
### Adding Data
The `add` method allows you to add elements to the cache. If the cache file is empty, the method creates a new cache file and writes the provided element to it. If the cache file already contains data, the method appends the new element to the existing data.
```java
 public static void main(String[] args) {
	 // Define a folder and file for the cache
	File folder = new File("cache-folder");
	File file = new File(folder, "cache-file.json");
	
	//Create an instance of JCache
	Cache<String> cache = new Cache<>(folder, file);
	
	//Add elements to the cache
	boolean added1 = cache.add("data1");
	boolean added2 = cache.add("data2");
	
	//Print the results
	System.out.println("Added data1: " + added1);
	System.out.println("Added data2: " + added2);
}
```

### Retrieving Data
The `get` method retrieves the cached data from the cache file and returns it as a list. You need to provide a TypeReference to specify the data type you want to retrieve. If the cache file is empty or unavailable, the method returns `null`.
```java
 public static void main(String[] args) {
     // Define a folder and file for the cache
     File folder = new File("cache-folder");
     File file = new File(folder, "cache-file.json");

     // Create an instance of JCache
     Cache<String> cache = new Cache<>(folder, file);

     // Get the cached data
     List<String> cachedData = cache.get(new TypeReference<>() {});

     // Print the cached data
     if (cachedData != null) {
         System.out.println("Cached Data: " + cachedData);
     } else {
         System.out.println("Cache is empty or unavailable.");
     }
 }
```

### Removing Data
The `remove` method allows you to remove an element from the cache at a specified index. If the cache file contains data and the removal is successful, the method updates the cache file with the modified data. If the cache file is empty or the removal fails, the method returns `false`.
```java
 public static void main(String[] args) {
     // Define a folder and file for the cache
     File folder = new File("cache-folder");
     File file = new File(folder, "cache-file.json");

     // Create an instance of JCache
     Cache<String> cache = new Cache<>(folder, file);

     // Add data to the cache
     cache.add("data1");
     cache.add("data2");

     // Remove an element from the cache
     boolean removed = cache.remove(0);

     // Print the result of removal
     if (removed) {
         System.out.println("Element removed successfully.");
     } else {
         System.out.println("Element removal failed or cache is empty.");
     }
 }
```
### Removing All Data
The `removeAll` method removes all elements from the cache by deleting the cache file and creating a new, empty cache file. This ensures that the cache file is empty after the operation.
```java
public static void main(String[] args) {  
	// Define a folder and file for the cache  
	File folder = new File("cache-folder");  
	File file = new File(folder, "cache-file.json");  
  
	// Create an instance of JCache  
	Cache<String> cache = new Cache<>(folder, file);  
  
	// Add data to the cache  
	cache.add("data1");  
	cache.add("data2");  
  
	// Remove all elements from the cache  
	cache.removeAll();  
  
	// Print a message indicating that all elements were removed  
	System.out.println("All elements removed from the cache.");  
}
```
## Contributions
Contributions are welcome! If you'd like to contribute to JCache, please follow these steps:
1. Fork the repository and create a new branch for your feature or bug fix.
2. Make your changes and submit a pull request.
3. Provide a clear description of your changes and their purpose.

## Licenses
JCache is licensed under the [MIT License](https://chat.openai.com/LICENSE).
