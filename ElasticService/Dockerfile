# Use the official Elasticsearch image
FROM docker.elastic.co/elasticsearch/elasticsearch:8.5.0

# Set environment variables for single-node setup
ENV discovery.type=single-node
ENV xpack.security.enabled=false
ENV xpack.security.enrollment.enabled=false



# Expose the Elasticsearch port
EXPOSE 9200 9300

# Define the entrypoint
ENTRYPOINT ["/bin/bash", "-c", "elasticsearch"]
