pub trait Draw {
    fn draw(&self);
}

pub struct Screen {
    pub components: Vec<Box<dyn Draw>>,
}

impl Screen {
    pub fn run(&self) {
        for component in self.components.iter(){
            component.draw();
        }
    }
}

// 这与定义使用了带有 trait bound 的泛型类型参数的结构体不同。
// 泛型类型参数一次只能替代一个具体类型，而 trait 对象则允许在运行时替代多种具体类型。
// 这限制了 Screen 实例必须拥有一个全是 Button 类型或者全是 TextField 类型的组件列表。
// 如果只需要同质（相同类型）集合，则倾向于使用泛型和 trait bound，因为其定义会在编译时采用具体类型进行单态化。
// pub struct Screen<T: Draw> {
//     pub components: Vec<T>,
// }
//
// impl<T> Screen<T>
//     where T: Draw {
//     pub fn run(&self) {
//         for component in self.components.iter() {
//             component.draw();
//         }
//     }
// }

pub struct Button {
    pub width: u32,
    pub height: u32,
    pub label: String,
}

impl Draw for Button {
    fn draw(&self) {

    }
}