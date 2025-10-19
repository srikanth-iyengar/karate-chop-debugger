#!/bin/bash

new_version=$(cat new_version.build)

git tag "v$new_version"

git push origin "v$new_version"