# auth

TODO: Description

## TODO: Template Notes

The generated project includes both a
`project.clj` (for [Leiningen](http://leiningen.org/)).

## Tasks

| Task                   |   Leiningen    |
|------------------------|----------------|
| Launch a REPL          | `lein repl`    |
| Run Tests              | `lein test`    |
| Launch a server        | `lein run`     |
| Build a deployable JAR | `lein uberjar` |

## Building a Docker container

```sh
# With Leiningen
$ lein uberjar

$ sudo docker build .
```

## Diagrams

![Alt text](https://raw.githubusercontent.com/labsoft-2018/auth/master/diagrams/carrier.register.svg?sanitize=true)

![Alt text](https://raw.githubusercontent.com/labsoft-2018/auth/master/diagrams/customer.auth.existing?sanitize=true)

![Alt text](https://raw.githubusercontent.com/labsoft-2018/auth/master/diagrams/customer.auth.fail.svg?sanitize=true)

![Alt text](https://raw.githubusercontent.com/labsoft-2018/auth/master/diagrams/customer.auth.new.svg?sanitize=true)

![Alt text](https://raw.githubusercontent.com/labsoft-2018/auth/master/diagrams/merchant.register.svg?sanitize=true)
