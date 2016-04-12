# jmh micro-bechmarks

JMH based micro-benchmarks comparing:
  * Java list, Scala immutable, and Scala mutable list. 
  * Java map, Scala immutable, and Scala mutable map. 
  * Json4s and Play json libraries. 

> performance numbers are averaged over 20 iterations.

#### Java & Scala List
For adding 1000 elements winners in order of performance are: java list, scala mutable list, scala immutable list.

```sh
Java list                    8553.295 ±  92.438  ns/op
Scala mutable list           8688.939 ± 136.757  ns/op
Scala immutable list         9897.755 ± 427.546  ns/op
```

#### Java & Scala Map
For adding 1000 entries winners in order of performance are: java map, scala mutable map, scala immutable map.

```sh
Java map                    22189.336 ± 259.899  ns/op
Scala mutable map           41996.687 ± 1369.807  ns/op
Scala immutable map        114364.004 ± 2363.975  ns/op
```

#### Json4s & Play json
For parsing a 19MB json file with nested data json4s is faster than play json.
```sh
Json4s      792.841 ± 107.084  ms/op
Play Json   954.414 ±  84.503  ms/op
```

For serializing a list of 100K case class containing nested objects play json is faster than json4s
```sh
Play Json   238.980 ±  8.750  ms/op
Json4s      479.076 ± 22.311  ms/op
```
