import argparse
import random
import string
import collections
import datetime
import time

def get_args():
    parser = argparse.ArgumentParser()

    parser.add_argument("count", type=int, default=100)
    parser.add_argument("output_file", type=str, default=100)

    return parser.parse_args()

def random_string(string_length=10):
    """Generate a random string of fixed length """
    letters = string.ascii_lowercase
    return ''.join(random.choice(letters) for i in range(string_length))

def random_float(order, max_dec):
    return round(random.random() * order, max_dec)

def generate_content(idx):
    ordered = collections.OrderedDict({
        "id": idx,
        "id_drone": random.randint(1, 9),
        "speed": random_float(10, 1),
        "altitude": random_float(20, 2),
        "latitude": random_float(100, 6),
        "longitude": random_float(100, 6),
        "datetime": '"{}"'.format(datetime.datetime.fromtimestamp(time.time())),
        "temperature": random_float(10, 1),
        "battery": random_float(100, 1),
        })
    return '{' +  "".join('"{}":{},'.format(key, val) for key, val in ordered.items())[:-1] + '}'

def generate_json_drone_msg(count, output_file):
    with open(output_file, "w") as out_fo:
        for idx in range(1, count + 1):
            content = generate_content(idx)
            out_fo.write("{content}\n".format(content=content))

def main():
    args = get_args()
    count = args.count
    output_file = args.output_file
    generate_json_drone_msg(count, output_file)

if __name__ == "__main__":
    main()

