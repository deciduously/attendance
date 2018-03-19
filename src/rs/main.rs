#![feature(plugin)]
#![plugin(rocket_codegen)]

extern crate attendance;
extern crate rocket;

#[cfg(test)] mod tests;

use attendance::db;
use attendance::handler::*;

fn rocket() -> rocket::Rocket {
    rocket::ignite()
        .manage(db::init_pool())
        .mount("/", routes![index, mock, files])
}

fn main() {
    rocket().launch();
}
