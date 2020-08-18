use serde::{Deserialize, Serialize};
use serde_json::Result;
use std::fs::File;
use std::io::{Write, Read};
use ron::ser::to_string;

fn main() {
    typed_example().unwrap();
    exercise_json().unwrap();
    exercise_ron().unwrap();
}

#[derive(Serialize, Deserialize)]
struct Person {
    name: String,
    age: u8,
    phones: Vec<String>,
}

#[derive(Serialize, Deserialize, Debug)]
enum Move {
    Up(u8),
    Down(u8),
    Left(u8),
    Right(u8),
}

impl Person {
    fn to_string(&self) -> String {
        let name = format!("Name: {}", self.name);
        let age = format!("Age: {}", self.age);
        let mut phones = String::from("Phones: \n");
        for phone in self.phones.iter() {
            phones.push('\t');
            phones.push_str(phone.as_str());
            phones.push('\n');
        }

        format!("{}\n{}\n{}", name, age, phones)
    }
}

fn typed_example() -> Result<()> {
    let data = r#"
        {
            "name": "John Doe",
            "age": 43,
            "phones": [
                "+44 1234567",
                "+44 2345678"
            ]
        }"#;

    let p: Person = serde_json::from_str(data)?;

    println!("{}", p.to_string());

    Ok(())
}

/// Exercise: Serialize and deserialize a data structure with serde (JSON).
/// [Link](https://github.com/pingcap/talent-plan/blob/master/rust/building-blocks/bb-2.md)
fn exercise_json() -> std::io::Result<()> {
    let a = Move::Up(5);
    let mut file = File::create("move.txt")?;
    file.write_all(serde_json::to_string(&a)?.as_ref())?;

    file = File::open("move.txt")?;
    let mut content = String::new();
    file.read_to_string(&mut content)?;
    let b: Move = serde_json::from_str(content.as_str()).unwrap();
    println!("{:?}", b);

    Ok(())
}

/// Exercise: Serialize and deserialize a data structure to a buffer with serde (RON).
/// [Link](https://github.com/pingcap/talent-plan/blob/master/rust/building-blocks/bb-2.md)
fn exercise_ron() -> Result<()>{
    let a = Move::Down(2);
    let serialize = serde_json::to_vec(&a)?;
    println!("{:?}", serialize);

    let str = std::str::from_utf8(&serialize).unwrap();
    let result = to_string(&str).expect("Serialization failed");
    println!("{}", result);

    Ok(())
}