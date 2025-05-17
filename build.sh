#!/bin/bash
set -e

echo "Building signed release AAB..."

./gradlew clean bundleRelease

echo "Verifying signing..."
jarsigner -verify -verbose -certs app/build/outputs/bundle/release/app-release.aab

echo "Done. AAB file is located at:"
echo "app/build/outputs/bundle/release/app-release.aab"
