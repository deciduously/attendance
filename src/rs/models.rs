#[derive(Queryable)]
pub struct Kid {
    pub id: i32,
    pub full_name: String,
    pub letter: String,
    pub mon: bool,
    pub tue: bool,
    pub wed: bool,
    pub thu: bool,
    pub fri: bool,
}
