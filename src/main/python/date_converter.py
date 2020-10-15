#!/usr/bin/env python3

import csv
import re

pattern = re.compile('(?P<month>\d+)/(?P<day>\d+)/(?P<year>\d+)')

with open ('warehouse_data.csv', 'r') as inputFile:
  reader = csv.reader(inputFile, delimiter=',', quotechar='\'')
  headers = next(reader)

  with open ('converted_warehouse_data.csv', 'w') as outputFile:
    writer = csv.writer(outputFile, delimiter=',', quotechar='\'', quoting=csv.QUOTE_MINIMAL)
    for row in reader:
      match = pattern.search(row[2])
      if match:
        row[2] = '20' + match.group('year') + '-' + match.group('month') + '-' + match.group('day')
      else:
        print(row)
      writer.writerow(row)
