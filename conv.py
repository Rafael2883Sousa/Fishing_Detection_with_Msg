import tensorflow as tf
import tensorflow_hub as hub

# Carregar o modelo pré-treinado do TensorFlow Hub
model_url = "https://tfhub.dev/google/nnlm-en-dim50/2"
embed = hub.KerasLayer(model_url)

# Definir uma função para pré-processar o texto
def embed_text(input):
    return embed(input)

# Converter o modelo para TensorFlow Lite
converter = tf.lite.TFLiteConverter.from_keras_model(embed)
tflite_model = converter.convert()

# Salvar o modelo TensorFlow Lite
with open("model.tflite", "wb") as f:
    f.write(tflite_model)
