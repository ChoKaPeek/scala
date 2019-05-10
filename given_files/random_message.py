import argparse
import random
import string

def get_args():
    parser = argparse.ArgumentParser()

    parser.add_argument("count", type=int, default=100)
    parser.add_argument("output_file", type=str, default=100)

    return parser.parse_args()

def random_string(string_length=10):
    """Generate a random string of fixed length """
    letters = string.ascii_lowercase
    return ''.join(random.choice(letters) for i in range(string_length))

def generate_content(idx):
    name = random_string(15)
    country = random_string(20)
    return '{' + '"id": {}, "name": "{}", "country": "{}"'.format(idx,
                                                                  name,
                                                                  country) + '}'

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

