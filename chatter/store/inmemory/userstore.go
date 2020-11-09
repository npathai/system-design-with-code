package inmemory

import "github.com/npathai/chatter/model"

type MemoryUserStore struct {
	users []*model.User
}

func (store *MemoryUserStore) GetAllUsers() ([]*model.User, error) {
	if store.users == nil {
		return []*model.User{}, nil
	}
	return store.users, nil
}

func (store *MemoryUserStore) Save(user *model.User) (*model.User, error) {
	// TODO assign ID to user
	store.users = append(store.users, user)
	return user, nil
}

func (store *MemoryUserStore) Get(userId string) (*model.User, error) {
	// TODO add code to get user by id
	return nil, nil
}