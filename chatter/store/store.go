package store

import "github.com/npathai/chatter/model"

type Store interface {
	User() UserStore
}

type UserStore interface {
	Save(user *model.User) (*model.User, error)
}
