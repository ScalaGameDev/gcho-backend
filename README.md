![Build Status](https://travis-ci.org/crimzie/gcho-backend.svg?branch=master)

## Scala backend for an online GURPS™ toolkit.

### Running in dev
* Have MongoDB running on local machine
* `sbt run`
* API description with Swagger: [http://127.0.0.1:9000/api](http://127.0.0.1:9000/api)
* Testing: `sbt test` and `newman run test/api_spec.json --ignore-redirects`

### Packaging
* `sbt universal:packageBin` produces a zip file in `target/universal/` with all the artifacts.

### Running in production
* Production configuration accepts several optional environment variables:
    - MONGO              - MongoDB URI (defaults to 127.0.0.1:27017)
    - GCHO_AUTH_SECRET   - encryption key for JWT tokens (defaults to a random string generated at startup)
    - GCHO_SIGNER_PEPPER - encryption pepper for JCA signer
    - GCHO_FBOOK_ID      - facebook oauth application id
    - GCHO_FBOOK_SECRET  - facebook oauth application secret
    - GCHO_GOOG_ID       - google oauth application id    
    - GCHO_GOOG_SECRET   - google oauth application secret        
    - GCHO_SMTP_HOST     - mail server hostname (defaults to 127.0.0.1)
    - GCHO_SMTP_PORT     - mail server port (defaults to 25)
    - GCHO_SMTP_LOGIN    - mail server login if required
    - GCHO_SMTP_PASSWORD - mail server password if required
* `./bin/gcho-backend -Dconfig.file=conf/prod.conf` starts the server.
