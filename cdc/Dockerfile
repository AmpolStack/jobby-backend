# Imagen base oficial de Debezium Connect
FROM debezium/connect:2.7.3.Final

# ====== AVRO CONVERTER DEPENDENCIAS ======
RUN mkdir -p /kafka/connect/avro-converter

RUN cd /kafka/connect/avro-converter && \
    curl -fSL https://packages.confluent.io/maven/io/confluent/kafka-connect-avro-converter/8.0.0/kafka-connect-avro-converter-8.0.0.jar -o kafka-connect-avro-converter-8.0.0.jar && \
    curl -fSL https://packages.confluent.io/maven/io/confluent/kafka-connect-avro-data/8.0.0/kafka-connect-avro-data-8.0.0.jar -o kafka-connect-avro-data-8.0.0.jar && \
    curl -fSL https://packages.confluent.io/maven/io/confluent/kafka-avro-serializer/8.0.0/kafka-avro-serializer-8.0.0.jar -o kafka-avro-serializer-8.0.0.jar && \
    curl -fSL https://packages.confluent.io/maven/io/confluent/kafka-schema-serializer/8.0.0/kafka-schema-serializer-8.0.0.jar -o kafka-schema-serializer-8.0.0.jar && \
    curl -fSL https://packages.confluent.io/maven/io/confluent/kafka-schema-converter/8.0.0/kafka-schema-converter-8.0.0.jar -o kafka-schema-converter-8.0.0.jar && \
    curl -fSL https://packages.confluent.io/maven/io/confluent/kafka-schema-registry-client/8.0.0/kafka-schema-registry-client-8.0.0.jar -o kafka-schema-registry-client-8.0.0.jar && \
    curl -fSL https://packages.confluent.io/maven/io/confluent/common-config/8.0.0/common-config-8.0.0.jar -o common-config-8.0.0.jar && \
    curl -fSL https://packages.confluent.io/maven/io/confluent/common-utils/8.0.0/common-utils-8.0.0.jar -o common-utils-8.0.0.jar && \
    curl -fSL https://repo1.maven.org/maven2/com/google/guava/guava/32.1.2-jre/guava-32.1.2-jre.jar -o guava-32.1.2-jre.jar && \
    curl -fSL https://repo1.maven.org/maven2/org/apache/avro/avro/1.12.1/avro-1.12.1.jar -o avro-1.12.1.jar

# ====== TU TRANSFORMADOR CUSTOM ======
# Crea una carpeta para tus transformadores
RUN mkdir -p /kafka/connect/custom

# Copia el JAR del transformador desde tu proyecto local
COPY ByteBufferToBytesTransform-1.0-jar-with-dependencies.jar /kafka/connect/custom/

# ====== CONFIGURACIÓN DE PLUGINS ======
# Asegura que Kafka Connect cargue los jars del avro converter + tus transformadores
ENV CONNECT_PLUGIN_PATH="/kafka/connect,/kafka/connect/avro-converter,/kafka/connect/custom"

# (Opcional) Define el host para REST de Kafka Connect
ENV CONNECT_REST_ADVERTISED_HOST_NAME=kafka-connect
