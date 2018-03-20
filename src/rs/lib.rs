#![feature(plugin)]
#![plugin(rocket_codegen)]

extern crate rocket;
#[macro_use] extern crate diesel;
extern crate r2d2_diesel;
extern crate r2d2;

pub mod csv;
pub mod db;
pub mod handlers;
