#!/bin/bash

new_version=$(cat new_version.build)
build_file="build.gradle.kts"

# Replace the line starting with 'version = ' safely
sed -i.bak -E "s/^version = \".*\"/version = \"$new_version\"/" "$build_file"

echo "Updated version in $build_file to $new_version"