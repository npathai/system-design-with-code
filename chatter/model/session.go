package model

type Session struct {
	Id string `json:"id"`
	UserId string `json:"user_id"`
	Token string `json:"token"`
	Props map[string]string
}

func (session *Session) GenerateCSRF() string {
	token := NewId()
	session.AddProp("csrf", token)
	return token
}

func (session *Session) AddProp(key, value string) {
	if session.Props == nil {
		session.Props = make(map[string]string)
	}
	session.Props[key] = value
}

func (session *Session) PreSave() {
	if session.Id == "" {
		session.Id = NewId()
	}

	if session.Token == "" {
		session.Token = NewId()
	}

	if session.Props == nil {
		session.Props = make(map[string]string)
	}
}



