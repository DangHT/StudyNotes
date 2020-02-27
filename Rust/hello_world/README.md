# 安装

Rust官方推荐使用 `rustup`作为Rust版本及关联工具的管理器。[官网下载](https://www.rust-lang.org/tools/install)

*注意：在Windows上安装Rust运行环境时，需要安装[Microsoft Visual C++ Build Tools](http://go.microsoft.com/fwlink/?LinkId=691126)*

使用`rustup`升级Rust环境

```cmd
rustup update
```

使用`rustup`卸载Rust环境

```cmd
rustup self uninstall
```

安装成功后，即可查看Rust版本

```cmd
rustc --version
```

# Hello World

新建main.rs

```rust
fn main() {
    println!("Hello World!");
}
```

编译运行

```cmd
rustc main.rs
./main.rs
```

`!`在Rust中表示宏指令，若不带`!`则表示普通的函数调用

# Cargo

`Cargo`是Rust的构建系统及包管理工具（类似于Java中的Maven或Gradle）

安装官方推荐的方法使用`rustup`安装Rust运行环境后，就已经附带安装了`Cargo`，可以直接在控制台查看版本

```cmd
cargo --version
```

使用`cargo`新建一个Rust项目

```cmd
cargo new hello_world
```

进入新建的项目文件夹下，`cargo`默认生成一个helloworld代码，可以直接编译运行

```cmd
cargo build //编译
./target/debug/hello_world //运行
```

也可以使用`cargo run`一条命令完成编译并运行的操作

```cmd
cargo run //编译并运行
```

在编写代码时，如果想要检查代码的正确性，`cargo`提供了`cargo check`命令可以快速检查代码的正确性，它的执行速度比`cargo build`更加快速，因为它跳过了生成.exe文件的阶段

# Cargo项目结构

使用`cargo`构建出的项目结构是这样的

```
hello_world
	|
	|___src
	|	 |__main.rs
	|
	|___.gitignore
	|
	|___Cargo.lock
	|
	|___Cargo.toml
```

src文件存分源码

Cargo.toml用于管理项目元数据及依赖信息（类似于Maven中的pom.xml）

```toml
[package]
name = "hello_world"
version = "0.1.0"
authors = ["DangHT <dht925nerd@126.com>"]
edition = "2018"

# See more keys and their definitions at https://doc.rust-lang.org/cargo/reference/manifest.html

[dependencies]

```

`cargo`自动从本地的Git中获取作者的姓名及邮箱信息，如果需要修改可以在Git中修改

Cargo.lock用于记录项目依赖引入的情况，在后续的构建过程中避免重复引入依赖

如果使用`cargo update`升级项目依赖，则会忽略Cargo.lock，但是使用`cargo update`升级时仅会进行小版本的升级不会进行大版本升级。例如：原项目某一依赖包版本为0.5.5，`cargo update`只会在 0.5.5 到 0.6.0之间进行升级，若要进行大版本更新需要在Cargo.toml中手动改写版本号

在使用`cargo build`编译项目后，编译生成的文件会在target/debug文件中，若项目准备正式发布可以使用`cargo build --release`命令，它在编译时会进行优化使得运行更快，但它也需要更多的编译时间，而且生成的文件会存在于target/release中