# Instances

> This project will keep `SNAPSHOT`, you can fork and release by yourself.

## Version

[![](https://jitpack.io/v/Zhupff/Instances.svg)](https://jitpack.io/#Zhupff/Instances)

## Download

Add `jitpack` maven.

```groovy
repositories {
    maven { url 'https://jitpack.io' }
}
```

Then add dependencies to module build.gradle.

```groovy
dependencies {
    implementation 'com.github.Zhupff.Instances:api:$version'
    annotationProcessor 'com.github.Zhupff.Instances:compiler:$version'
    // also support kapt and ksp.
}
```

## Usage

It works by generating files in `META-INF/services/` just like [AutoService](https://github.com/google/auto/tree/master/service), so please try not to use both with the same interface at the same time.

See the usage in **sample**, which is very simple.

## LICENSE

```markdown
Copyright 2022 Zhupff

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```