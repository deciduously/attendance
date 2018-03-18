#![feature(plugin)]
#![plugin(rocket_codegen)]

extern crate rocket;
#[macro_use] extern crate diesel;
extern crate r2d2_diesel;
extern crate r2d2;
#[macro_use] extern crate dotenv_codegen;

pub mod db;
pub mod handler;
pub mod models;
pub mod schema;
