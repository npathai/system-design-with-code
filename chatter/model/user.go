package model

type User struct {
	Id	string	`json:"id"`
	TeamId             string    `json:"team_id"`
	Username           string    `json:"username"`
	Password           string    `json:"password"`
	AuthData           string    `json:"auth_data"`
	Email              string    `json:"email"`
	FullName           string    `json:"full_name"`
}
