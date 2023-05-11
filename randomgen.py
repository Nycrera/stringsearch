import random

def generate_random_bitstring(n):
    bitstring = ''.join(random.choice('01') for _ in range(n))
    return bitstring

def write_bitstring_to_file(bitstring, filename):
    with open(filename, 'w') as file:
        file.write("<html><body>")
        file.write(bitstring)
        file.write("</body></html>")

bitstring_length = 1024*1024 # 1 mb file
random_bitstring = generate_random_bitstring(bitstring_length)
write_bitstring_to_file(random_bitstring, 'test_bitstring.html')
print("Done.")