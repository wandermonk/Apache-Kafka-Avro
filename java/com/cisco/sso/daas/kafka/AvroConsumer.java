package com.cisco.sso.daas.kafka;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

import org.apache.avro.Schema;
import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

public class AvroConsumer<T> {

	private static Properties kafkaProps;

	static {
		kafkaProps = new Properties();
		kafkaProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		kafkaProps.put("bootstrap.servers", "localhost:9092");
		kafkaProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		kafkaProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class);
		kafkaProps.put(ConsumerConfig.GROUP_ID_CONFIG, "AvroConsumer-GroupOne");
	}

	public void recieveRecord() throws IOException {
		try (KafkaConsumer<String, byte[]> kafkaConsumer = new KafkaConsumer<>(kafkaProps)) {
			kafkaConsumer.subscribe(Arrays.asList("jason"));
			while (true) {
				ConsumerRecords<String, byte[]> records = kafkaConsumer.poll(100);
				Schema.Parser parser = new Schema.Parser();
				final Schema schema = parser
						.parse(AvroProducer.class.getClassLoader().getResourceAsStream("syslog.avsc"));
				records.forEach(record -> {
					SpecificDatumReader<T> datumReader = new SpecificDatumReader<>(schema);
					ByteArrayInputStream is = new ByteArrayInputStream(record.value());
					BinaryDecoder binaryDecoder = DecoderFactory.get().binaryDecoder(is, null);
					try {
						T log = datumReader.read(null, binaryDecoder);
						System.out.println("Value: " + log);
					} catch (IOException e) {
						e.printStackTrace();
					}
				});
			}
		}
	}

}
