#![feature(plugin)]
#![plugin(rocket_codegen)]

extern crate attendance;
extern crate diesel;
extern crate rocket;

#[cfg(test)] mod tests;

use attendance::db::init::init_pool;
use attendance::handlers::*;

fn rocket() -> rocket::Rocket {
    rocket::ignite()
        .manage(init_pool())
        .mount("/", routes![index, mock, files])
}

fn main() {
    rocket().launch();
}
