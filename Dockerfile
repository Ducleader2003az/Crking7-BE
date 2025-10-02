# Bước 1: Chọn base image với OpenJDK 17
FROM openjdk:17-jdk-alpine

# Bước 2: Đặt biến môi trường cho tên của tệp JAR
ARG JAR_FILE=target/*.jar

# Bước 3: Copy file JAR vào Docker image
COPY ${JAR_FILE} app.jar

# Bước 4: Đặt cổng mặc định để chạy ứng dụng
EXPOSE 8081

# Bước 5: Chạy ứng dụng
ENTRYPOINT ["java", "-jar", "/app.jar"]