#!/bin/bash

export LC_ALL=C.UTF-8
export LANG=C.UTF-8

mlflow server --host 0.0.0.0  2>&1 | tee server.log
