This is a program to validate files content by checksum comparison.

### Building from source
Install and configure GraalVM. Run:
```
./gradlew nativeCompile
```

### Usage
Run from command line (if `<path>` is not specified, it defaults to the current directory): 
```
./dir-checksum <path>
```

On the first run `checksums.txt` file will be created with content like this:
```
build.gradle.kts - 2024-12-24 14:00:01 - 464.0B - 3196c19d21a2ac1e62be4f292afa04b84b56d5bfe4749441ec006660b367ea0a
gradlew - 2024-12-24 14:00:01 - 7.9KB - f96a757581fe5292465e3f3393380bd11fd8086d450b92e7c693da6513f583fe
gradlew.bat - 2024-12-24 14:00:01 - 2.7KB - af835f98787e9269af5a046edcb821a592fed372139df7b947b471a63cfc236b
readme.md - 2024-12-26 11:53:03 - 1.1KB - 45ada9dd0417959c8dc3dc2afd5cf059bbf57729620f35b760ed5a6a492ce212
settings.gradle.kts - 2024-12-24 14:00:01 - 129.0B - b84319a9ee4e89150e5567dee1e313ec6fbf478f431a2b72137f317edac28a69
```

If `checksums.txt` already exists, its content is compared to the calculated checksums
and in case of any difference current checksums are written to `checksums-actual.txt`.
