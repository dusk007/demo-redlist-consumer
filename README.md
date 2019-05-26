# Demo App Conume RedList API

### Example Definition
1. Load the list of the available regions for species
2. Take a random region from the list
3. Load the list of all species in the selected region — the first page of the results would
suffice, no need for pagination
4. Create a model for “Species” and map the results to an array of Species.
5. Filter the results for Critically Endangered species
1. Fetch the conservation measures for all critically endangered species
2. Store the “title”-s of the response in the Species model as concatenated text property.
3. Print/display the results
6. Filter the results (from step 4) for the mammal class
1. Print/display the results

### Guides
DemoRedisApplicationTests contains the functionality
```bash
mvn test -Dtest=DemoRedlistApplicationTests
```
Basic functions are implemented in sync.
Step 6 should be async calls.

### Notes

I used kotlin because it looks nice  
and all the code used here can be used in pure java  
all dependencies used are java and not kotlin specific.

In Java I usually create multiple packages and one file per model.
In Kotlin one models.kt file with the data classes,  
helps keeping the relevant info in fewer files.

RedListConsumerTest just tests the http calls.

DemoRedlistApplicationTests executes the steps for the example.
I first implemented just the minimum requirements with just the collections library.

Normally I would add Caches and/or lazy inits so I did ;).
Step 6 calls the api multiple times thus not implementing this async would be a crime.
I am most familiar with rxjava2. There are obviously plenty of alternative ways.

I'd probably also use [tablesaw](https://github.com/jtablesaw/tablesaw) or spark-streaming instead of ArrayLists for fast filter operations and lower memory usage.
