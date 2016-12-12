#!/bin/bash
for i in {1..100}
do
  curl -vvv -X POST "http://localhost:8080/customer/1?firstName=Pieter$i"
done

