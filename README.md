# BackEnd

## Jenkins Pipeline stages
```sh
pipeline
 ├─ environment
 ├─ stages
 │   ├─ Environment Info
 │   ├─ Clean Workspace
 │   ├─ Git Checkout
 │   ├─ Maven Build
 │   ├─ SonarQube
 │   ├─ Security Scans
 │   ├─ Docker Build
 │   ├─ Docker Test
 │   ├─ Trivy Image Scan
 │   ├─ Push to Registry
 │   └─ Deploy to server
 └─ post
```