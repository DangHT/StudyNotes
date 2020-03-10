struct User {
    username: String,
    email: String,
    sign_in_count: u64,
    active: bool,
}

struct Color(i32, i32, i32);
struct Point(i32, i32, i32);

#[derive(Debug)]
struct Rectangle {
    width: u64,
    height: u64,
}

impl Rectangle {
    fn perimeter(&self) -> u64 {
        self.height * 2 + self.width * 2
    }

    fn area(&self) -> u64 {
        self.width * self.height
    }

    fn can_hold(&self, other: &Rectangle) -> bool {
        self.width > other.width && self.height > other.height
    }

    fn square(size: u64) -> Rectangle {
        Rectangle { width: size, height: size }
    }
}

fn build_user(username: String, email: String) -> User {
    User {
        username,
        email,
        sign_in_count: 1,
        active: true,
    }
}

fn main() {
    let user1 = User {
        username: String::from("DangHT"),
        email: String::from("dht925nerd@126.com"),
        sign_in_count: 1,
        active: true,
    };

    let user2 = User {
        username: String::from("Mary"),
        email: String::from("mary@123.com"),
        ..user1
    };

    let black = Color(0, 0, 0);
    let origin = Point(0, 0, 0);

    let rec1 = Rectangle { width: 30, height: 50 };
    let rec2 = Rectangle { width: 10, height: 20 };
    println!("rec is {:?}, area = {}, perimeter = {}", rec1, area(&rec1), rec1.perimeter());
    println!("more good looking format:\nrec is {:#?}", rec1);
    println!("{}", rec1.can_hold(&rec2));

    //关联函数
    let sqr = Rectangle::square(5);
    println!("Square is {:#?}", sqr);
}

//借用结构体而不是获得它的所有权，这样main当area调用结束后
//main函数可以保持对结构体的所有权继续使用它，所以这里参数加上&
fn area(rectangle: &Rectangle) -> u64 {
    rectangle.width * rectangle.height
}
