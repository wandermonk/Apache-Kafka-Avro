package com.cisco.sso.daas.kafka;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.PropertyAccessorFactory;

public class AvroProducer<T> {

	private static Properties props;
	static {
		props = new Properties();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class);
		props.put(ProducerConfig.CLIENT_ID_CONFIG, "Kafka Avro Producer");
	}
	
	private static KafkaProducer<StringSerializer,byte[]> producer = new KafkaProducer<>(props);
	
	public byte[] createRecords(T pojo) throws IOException{
		Schema.Parser parser = new Schema.Parser();
		Schema schema = null;
		try {
			schema = parser.parse(AvroProducer.class.getClassLoader().getResourceAsStream("syslog.avsc"));
		} catch (IOException e) {
			System.out.println(e.getLocalizedMessage());
		}
		
        final GenericData.Record record = new GenericData.Record(schema);
        schema.getFields().forEach(r -> record.put(r.name(), PropertyAccessorFactory.forDirectFieldAccess(pojo).getPropertyValue(r.name())));
		SpecificDatumWriter<GenericRecord> writer = new SpecificDatumWriter<>(schema);
		try(ByteArrayOutputStream os = new ByteArrayOutputStream()){
			BinaryEncoder encoder = EncoderFactory.get().binaryEncoder(os, null);
			
			writer.write(record, encoder);
			encoder.flush();
			byte[] bytemessage = os.toByteArray();
			return bytemessage;
		}
		
	}
	
	public static void sendMessage(byte[] bytemessage){
		ProducerRecord<StringSerializer,byte[]> precord = new ProducerRecord<StringSerializer, byte[]>("jason", bytemessage);
		producer.send(precord);
	}
}
