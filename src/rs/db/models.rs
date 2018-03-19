use super::schema::kids;

// TODO make a brand new type for a schedule block!
// Either for a single day "8-6" or a whole week and let Diesel handle the data structure.
#[derive(Queryable)]
pub struct Kid {
    pub id: i32,
    pub full_name: String,
    pub letter: String,
    pub mon: String,
    pub tue: String,
    pub wed: String,
    pub thu: String,
    pub fri: String,
}

#[derive(Insertable)]
#[table_name="kids"]
pub struct NewKid<'a> {
    pub full_name: &'a str,
    pub letter: &'a str,
    pub mon: &'a str,
    pub tue: &'a str,
    pub wed: &'a str,
    pub thu: &'a str,
    pub fri: &'a str,
}
