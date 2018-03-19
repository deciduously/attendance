use super::rocket;
use rocket::local::Client;
use rocket::http::Status;

#[test]
fn handlers() {
    let client = Client::new(rocket()).expect("valid rocket instance");
    let mut response = client.get("/mock/a").dispatch();
    assert_eq!(response.status(), Status::Ok);
    assert_eq!(response.body_string(), Some(String::from("No such mock resource - try roster or extra")));
}
