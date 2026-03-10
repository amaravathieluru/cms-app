FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY cms/ .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

5. Commit directly to main

---

## 🔧 PHASE 2: Clean Up Railway

### **STEP 4: Delete ALL variables on Railway**
1. Go to Railway → **heartfelt-respect** → **Variables tab**
2. Delete **every single variable** one by one
3. We don't need them anymore since everything is hardcoded

### **STEP 5: Clear Settings**
1. Go to **Settings → Build**
2. **Clear** the Custom Build Command field (make it empty)
3. Go to **Settings → Deploy**
4. **Clear** the Custom Start Command field (make it empty)
5. Railway will now use the **Dockerfile** automatically

---

## 🔧 PHASE 3: Deploy

### **STEP 6: Trigger New Deploy**
1. Go to **Deployments tab**
2. Click **"..."** on the latest deployment
3. Click **"Redeploy"**

### **STEP 7: Watch Build Logs**
You should now see:
```
✅ Detected Dockerfile
✅ Building Docker image...
✅ FROM maven:3.9.6-eclipse-temurin-21
✅ RUN mvn clean package -DskipTests
✅ BUILD SUCCESS
✅ Deploying...
